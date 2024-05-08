package com.fuze.bcp.msg.business;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.EmployeeLookupBean;
import com.fuze.bcp.api.bd.bean.OrgBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.file.bean.EmailObjectBean;
import com.fuze.bcp.api.file.bean.PushObjectBean;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.api.msg.bean.*;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.exception.MessageMqException;
import com.fuze.bcp.msg.domain.*;
import com.fuze.bcp.msg.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.service.TemplateService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.utils.StringHelper;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/7/20.
 */
@Service
public class BizMessageService implements IMessageBizService {

    private Logger logger = LoggerFactory.getLogger(BizMessageService.class);

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    IMessageTemplateService iMessageTemplateService;

    @Autowired
    IMessageSubscribeService iMessageSubscribeService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    INoticeService iNoticeService;

    @Autowired
    IMessageLogService iMessageLogService;

    @Autowired
    ISubScribeSourceService iSubScribeSourceService;

    @Autowired
    ITemplateBizService iTemplateBizService;

    @Autowired
    ISMSService ismsService;

    @Autowired
    IEmailService iEmailService;

    @Autowired
    IFeedbackService iFeedbackService;

    @Autowired
    TemplateService templateService;

    @Autowired
    private IParamBizService iParamBizService;

    /**
     * 获取整理后的待发送信息
     * userContents 订阅用户对应的发送信息Map<发送目标，如邮件地址、电话号码等, Map<发送类型, 根据类型不同存储的不同类型的值>>
     *
     * @param businessType
     * @param transactionId
     * @param templateData
     * @return
     * @throws Exception
     */
    @Override
    public ResultBean<Map<String, Map<String, Object>>> actGetSendMap(String businessType, String transactionId, Map templateData, List<String> fileIds) throws MessageMqException {
        List<MessageSubScribe> subScribes = iMessageSubscribeService.getListByBusinessType(businessType);
        if (subScribes == null || subScribes.size() == 0)
            throw new MessageMqException("无人订阅此类消息，businessEventType：" + businessType + "transactionId：" + transactionId);
        List<MessageTemplateBean> messageTemplateBeans = this.actGetTemplateByBusinessType(businessType).getD();
        if (messageTemplateBeans == null || messageTemplateBeans.size() == 0)
            throw new MessageMqException("此类消息暂无模板可用，businessEventType：" + businessType + "transactionId：" + transactionId);
        Map<String, Map<String, Object>> userContents = new HashMap<>();
        subScribes.forEach(s -> {
            for (String sourceId : s.getSubScribeSourceIds()) {
                Map<String, Object> content = this.getMessageMap(s.getUserChannel(), messageTemplateBeans, transactionId, templateData, fileIds);
                Map<String, Object> contents = userContents.get(s.getScribeType() + "." + sourceId);
                if (contents == null) {
                    contents = new HashMap<String, Object>();
                }
                contents.putAll(content);
                userContents.put(s.getScribeType() + "." + sourceId, contents);
            }
        });
        return ResultBean.getSucceed().setD(userContents);
    }

    @Override
    public ResultBean<DataPageBean<MessageTemplateBean>> actGetMessageTemplates(Integer currentPage) {
        Page<MessageTemplate> messageTemplates = iMessageTemplateService.getAllOrderByBusinessType(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(messageTemplates, MessageTemplateBean.class));
    }

    @Override
    public ResultBean<MessageTemplateBean> actGetMessageTemplate(String templateId) {
        return ResultBean.getSucceed().setD(mappingService.map(iMessageTemplateService.getOne(templateId), MessageTemplateBean.class));
    }

    @Override
    public ResultBean<String> actGetMessageFromTemplate(String templateId, Map params) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
        configuration.setTemplateLoader(iMessageTemplateService);
        Template temp = configuration.getTemplate(templateId);
        StringWriter writer = new StringWriter();
        try {
            temp.process(params, writer);
            return ResultBean.getSucceed().setD(writer.toString());
        } catch (TemplateException e) {
            logger.error(e.getMessage(), e);
            return ResultBean.getFailed().setM("模板输出内容ERROR");
        }

    }

    @Override
    public ResultBean<MessageTemplateBean> actSaveMessageTemplate(MessageTemplateBean messageTemplateBean) {
        MessageTemplate messageTemplate = mappingService.map(messageTemplateBean, MessageTemplate.class);
        return ResultBean.getSucceed().setD(mappingService.map(iMessageTemplateService.save(messageTemplate), MessageTemplateBean.class));
    }

    @Override
    public ResultBean<MessageTemplateBean> actDeleteMessageTemplate(String templateId) {
        MessageTemplate messageTemplate = iMessageTemplateService.getOne(templateId);
        if (messageTemplate != null) {
            iMessageTemplateService.delete(templateId);
            return ResultBean.getSucceed().setD(mappingService.map(messageTemplate, MessageTemplateBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<MessageTemplateBean>> actGetTemplateByBusinessType(String messageType) {
        List<MessageTemplate> messageTemplates = iMessageTemplateService.getTemplateByDataStatusAndBusinessType(messageType);
        return ResultBean.getSucceed().setD(mappingService.map(messageTemplates, MessageTemplateBean.class));
    }

    @Override
    public ResultBean<MessageLogBean> actSaveMessageLog(MessageLogBean messageLogBean) {
        MessageLog messageLog = mappingService.map(messageLogBean, MessageLog.class);
        messageLog = iMessageLogService.save(messageLog);
        return ResultBean.getSucceed().setD(mappingService.map(messageLog, MessageLogBean.class));
    }

    @Override
    public ResultBean<List<MessageLogBean>> actGetMessageLogs(List<String> logIds) {
        List<MessageLog> messageLogs = iMessageLogService.getAvaliableList(logIds);
        return ResultBean.getSucceed().setD(mappingService.map(messageLogs, MessageLogBean.class));
    }

    @Override
    public ResultBean<MessageLogBean> actGetMessageLog(String id) {
        MessageLog messageLog = iMessageLogService.getOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(messageLog, MessageLogBean.class));
    }

    @Override
    public ResultBean<String> actSendSMS(List<String> cells, String content) {
        MessageLog messageLog = new MessageLog();
        Boolean canSendMsg = iParamBizService.actGetBoolean("CAN_SEND_MSG").getD();
        if (canSendMsg == null) {
            messageLog.setResult(MessageLogBean.FAILD);
            messageLog = iMessageLogService.save(messageLog);
            return ResultBean.getFailed().setD(messageLog.getId()).setM(String.format(messageService.getMessage("MSG_SYSPARAM_NOTFIND_NULL"), "CAN_SEND_MSG"));
        }
        if (!canSendMsg) {
            messageLog.setResult(MessageLogBean.FAILD);
            messageLog = iMessageLogService.save(messageLog);
            return ResultBean.getFailed().setD(messageLog.getId()).setM("不发送短信！");
        }

        String result = ismsService.send(cells, content);
        messageLog.setTo(cells.toString());
        Map map = new HashMap<>();
        map.put("content", content);
        messageLog.setContent(map);
        if (result.startsWith("success")) {
            messageLog.setResult(MessageLogBean.SUCCESS);
            messageLog = iMessageLogService.save(messageLog);
            return ResultBean.getSucceed().setD(messageLog.getId());
        } else {
            messageLog.setResult(MessageLogBean.FAILD);
            messageLog = iMessageLogService.save(messageLog);
            return ResultBean.getFailed().setD(messageLog.getId()).setM("短信接口返回faild");
        }
    }

    @Override
    public ResultBean<String> actSendEmails(List<String> tos, Map emailInfo) {
        MessageLog messageLog = new MessageLog();
        if (emailInfo == null) {
            return ResultBean.getFailed();
        }
        try {
            iEmailService.send(tos, emailInfo.get("subject").toString(), emailInfo.get("body").toString(), (List<String>) emailInfo.get("fileIds"));
            for (String to : tos) {
                messageLog.setTo(to);
                messageLog.setContent(emailInfo);
                messageLog.setResult(MessageLogBean.SUCCESS);
                messageLog = iMessageLogService.save(messageLog);
            }
            return ResultBean.getSucceed().setD(messageLog.getId());
        } catch (Exception e) {
            logger.error("邮件发送失败", e);
            messageLog.setResult(MessageLogBean.FAILD);
            messageLog = iMessageLogService.save(messageLog);
            return ResultBean.getFailed().setD(messageLog.getId());
        }
    }

    @Override
    public ResultBean<String> actSendEmail(String to, Map emailInfo) {
        MessageLog messageLog = new MessageLog();
        List<String> tos = new ArrayList<>();
        tos.add(to);
        try {
            iEmailService.send(tos, emailInfo.get("subject").toString(), emailInfo.get("body").toString(), (List<String>) emailInfo.get("fileIds"));
            messageLog.setTo(to);
            messageLog.setContent(emailInfo);
            messageLog.setResult(MessageLogBean.SUCCESS);
            messageLog = iMessageLogService.save(messageLog);
            return ResultBean.getSucceed().setD(messageLog.getId());
        } catch (Exception e) {
            logger.error("邮件发送失败", e);
            messageLog.setResult(MessageLogBean.FAILD);
            messageLog = iMessageLogService.save(messageLog);
            return ResultBean.getFailed().setD(messageLog.getId());
        }
    }

    @Override
    public ResultBean<FeedbackBean> actSaveFeedBack(FeedbackBean feedbackBean) {
        Feedback feedback = iFeedbackService.save(mappingService.map(feedbackBean, Feedback.class));
        if (feedback == null || StringHelper.isBlock(feedback.getId())) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FEEDBACK_SAVE_ERROR"));
        } else {
            return ResultBean.getSucceed().setD(mappingService.map(feedback, FeedbackBean.class)).setM(messageService.getMessage("MSG_FEEDBACK_SAVE_SUCESS"));
        }
    }

    @Override
    public ResultBean<DataPageBean<FeedbackBean>> actGetFeedBacks(Integer currentPage) {
        Page<Feedback> feedbacks = iFeedbackService.getAllOrderByTs(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(feedbacks, FeedbackBean.class));
    }

//    @Override
//    public ResultBean<DataPageBean<FeedbackBean>> actGetUserFeedBacks(Integer currentPage, String loginUsrId) {
//        Page<Feedback> feedbacks = iFeedbackService.getAvaliableBaseAll(currentPage, loginUsrId);
//        return ResultBean.getSucceed().setD(mappingService.map(feedbacks, FeedbackBean.class));
//    }

//    @Override
//    public ResultBean<List<FeedbackBean>> actDetailFeedback(String rootId) {
//        List<Feedback> feedbacks = new ArrayList<>();
//        feedbacks.add(iFeedbackService.getAvailableOne(rootId));
//        feedbacks.addAll(iFeedbackService.getFeedbackListByRootId(rootId));
//        return ResultBean.getSucceed().setD(mappingService.map(feedbacks, FeedbackBean.class));
//    }

    @Override
    public ResultBean<FeedbackBean> actDeleteFeedback(String id) {
        return ResultBean.getSucceed().setD(iFeedbackService.delete(id));
    }


    @Override
    public ResultBean<NoticeBean> actGetNotice(String id) {
        Notice notice = iNoticeService.getAvailableOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(notice, NoticeBean.class));
    }

    @Override
    public ResultBean<DataPageBean<NoticeBean>> actGetNotices(Integer currentPage) {
        Page page = iNoticeService.getAllByTsDesc(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(page, NoticeBean.class));
    }

    @Override
    public ResultBean<NoticeBean> actSaveNotice(NoticeBean noticeBean) {
        Notice notice = iNoticeService.save(mappingService.map(noticeBean, Notice.class));
        return ResultBean.getSucceed().setD(mappingService.map(notice, NoticeBean.class));
    }

    @Override
    public ResultBean<NoticeBean> actDeleteNotice(String id) {
        Notice notice = iNoticeService.delete(id);
        return ResultBean.getSucceed().setD(mappingService.map(notice, NoticeBean.class));
    }

    /**
     * 组装map key为发送类型，根据发送类型key不同，value不同
     *
     * @param channels
     * @param messageTemplateBeans
     * @param transactionId
     * @param templateData
     * @return
     */
    private Map<String, Object> getMessageMap(List<String> channels, List<MessageTemplateBean> messageTemplateBeans, String transactionId, Map templateData, List<String> fileIds) {
        Map<String, Object> contents = new HashMap<>();
        messageTemplateBeans.forEach(mt -> {
            String sendType = mt.getSendType();
            if (channels.contains(sendType)) { // 模板偏好同时存在
                Object value = null;
                switch (sendType) { // 不同发送类型生成不同的内容value
                    case Channels.EMAIL:
                        try {
                            value = getEmailValue(transactionId, mt, templateData, fileIds);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (TemplateException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Channels.WEBSOCKET:
                        value = getSimpleValue(transactionId, mt, templateData);
                        break;
                    case Channels.WECHAT:
                        break;
                    case Channels.SMS:
                        value = getSimpleValue(transactionId, mt, templateData);
                        break;
                    case Channels.PAD:
                        value = getPushValue(transactionId, mt, templateData);
                        break;
                }
                if (value != null && !"".equals(value)) {
                    contents.put(sendType, value);
                }
            }
        });
        return contents;
    }

    private Map getEmailValue(String transactionId, MessageTemplateBean mt, Map templateData, List<String> fileIds) throws IOException, TemplateException {
        Map<String, Object> emailInfo = new HashMap<>();
        EmailObjectBean emailObjectBean = iTemplateBizService.actGetEmailObject(mt.getEmailObjectId()).getD();
        String body = iTemplateBizService.actCreateStrByTemplate(emailObjectBean.getContentTemplateId(), transactionId, templateData).getD();
        emailInfo.put("body", body);
        StringTemplateLoader stl = new StringTemplateLoader();
        stl.putTemplate("template", emailObjectBean.getSubject());
        List<String> dataMap = new ArrayList<String>();
        dataMap.add("customer");
        dataMap.add("cashsource");
        Map tpDataMap = templateService.getMetaDatas(transactionId, dataMap);
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(stl);
        Template temp = configuration.getTemplate("template");
        StringWriter writer = new StringWriter();
        temp.process(tpDataMap, writer);
        emailInfo.put("subject", writer.toString());
        emailInfo.put("fileIds", fileIds);
        return emailInfo;
    }

    private Map getPushValue(String transactionId, MessageTemplateBean mt, Map templateData) {
        Map<String, Object> pushInfo = new HashMap<>();
        PushObjectBean pushObjectBean = iTemplateBizService.actGetPushObject(mt.getPushObjectId()).getD();
        String body = iTemplateBizService.actCreateStrByTemplate(pushObjectBean.getContentTemplateId(), transactionId, templateData).getD();
        pushInfo.put("body", body);
        pushInfo.put("subject", pushObjectBean.getSubject());
        return pushInfo;
    }

    private String getSimpleValue(String transactionId, MessageTemplateBean mt, Map templateData) {
        String templateId = mt.getId();
        try {
            Map tpDataMap = templateService.getMetaDatas(transactionId, mt.getMetaDatas());
            if (templateData != null) {
                tpDataMap.putAll(templateData);
            }
            return this.actGetMessageFromTemplate(templateId, tpDataMap).getD();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ResultBean<DataPageBean<MessageSubScribeBean>> searchMessageSubScribes(Integer currentPage, MessageSubScribeBean messageSubScribeBean) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").gt(DataStatus.TEMPSAVE));
        if (!StringUtils.isEmpty(messageSubScribeBean.getBusinessType()))
            query.addCriteria(Criteria.where("businessType").is(messageSubScribeBean.getBusinessType()));
        if (!StringUtils.isEmpty(messageSubScribeBean.getUserChannel()) && messageSubScribeBean.getUserChannel().size() > 0)
            query.addCriteria(Criteria.where("userChannel").in(messageSubScribeBean.getUserChannel().get(0)));
        Pageable pageable = new PageRequest(currentPage, 20);
        query.with(pageable);
        List list = mongoTemplate.find(query,MessageSubScribe.class);
        Page<MessageSubScribe> messageSubScribes = new PageImpl(list,pageable, mongoTemplate.count(query,MessageSubScribe.class));
        return ResultBean.getSucceed().setD(mappingService.map(messageSubScribes, MessageSubScribeBean.class));
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    IOrgBizService iOrgBizService;

    @Override
    public ResultBean<DataPageBean<NoticeLookupBean>> actGetMyNoticeForPad(Integer currentPage, Integer pageSize, String type, String sendType, String loginUserName) {
        LoginUserBean loginUserBean = iAuthenticationBizService.actFindByUsername(loginUserName).getD();
        EmployeeLookupBean employeeLookupBean = iOrgBizService.actGetEmployeeByLogin(loginUserBean.getId()).getD();
        if (NoticeBean.TYPE_1.equals(type)) { // 通知
            List<String> groupIds = loginUserBean.getUserRoleIds();
            String orgId = employeeLookupBean.getOrgInfoId();
            List<String> orgIds = new ArrayList<>();
            OrgBean orgBean = iOrgBizService.actGetOrg(orgId).getD();
            orgIds.add(orgBean.getId());
            if (orgBean.getVirtual()) {
                String parentId = iOrgBizService.actGetOrg(orgBean.getParentId()).getD().getId();
                orgIds.add(parentId);
            }
            Criteria c = new Criteria();
            Criteria ci = new Criteria();
            ci.orOperator(Criteria.where("loginUserNames").is(employeeLookupBean.getId()), Criteria.where("groupId").in(groupIds), Criteria.where("orgId").in(orgIds));
            c.and("dataStatus").is(DataStatus.SAVE).and("status").is(3).and("type").is(type).and("sendType").is(sendType).andOperator(ci);
            Query query = new Query(c);
            Pageable pageable = new PageRequest(currentPage, pageSize, new Sort(Sort.Direction.DESC, "sendTime"));
            query.with(pageable);
            List<Notice> notices = mongoTemplate.find(query, Notice.class);
            Page page = new PageImpl(notices, pageable, mongoTemplate.count(query, Notice.class));
            return ResultBean.getSucceed().setD(mappingService.map(page, NoticeLookupBean.class));
        } else if (NoticeBean.TYPE_2.equals(type)) { // 公告
            Page<Notice> notices = iNoticeService.findByDataStatusAndStatusAndTypeAndSendType(currentPage, pageSize, 3, type, sendType);
            return ResultBean.getSucceed().setD(mappingService.map(notices, NoticeLookupBean.class));
        } else if (NoticeBean.TYPE_3.equals(type)) { // 消息
            Page<Notice> notices = iNoticeService.findByTypeAndSendTypeAndLoginUserName(currentPage, pageSize, 3, type, sendType, employeeLookupBean.getId());
            return ResultBean.getSucceed().setD(mappingService.map(notices, NoticeLookupBean.class));
        }
        return ResultBean.getFailed();
    }
}
