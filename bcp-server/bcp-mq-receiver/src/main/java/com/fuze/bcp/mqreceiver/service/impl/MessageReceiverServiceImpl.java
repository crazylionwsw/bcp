package com.fuze.bcp.mqreceiver.service.impl;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.msg.bean.Channels;
import com.fuze.bcp.api.msg.bean.MessageLogBean;
import com.fuze.bcp.api.msg.bean.NoticeBean;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.api.msg.service.ISubscribeBizService;
import com.fuze.bcp.api.push.bean.PushDataBean;
import com.fuze.bcp.api.push.service.IPushDataBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.exception.MessageMqException;
import com.fuze.bcp.mqreceiver.service.IMessageReceiverService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by CJ on 2017/7/7.
 */

@Service
public class MessageReceiverServiceImpl implements IMessageReceiverService {

    Logger logger = LoggerFactory.getLogger(MessageReceiverServiceImpl.class);

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    IMessageBizService iMessageBizService;

    @Autowired
    ISubscribeBizService iSubscribeBizService;

    @Autowired
    IPushDataBizService iPushDataBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void process(MsgRecordBean msgRecord) throws IOException {
        if (msgRecord.getBizFlag()) {
            msgRecord.setStatus(MsgRecordBean.STATUS_EXECUTE);
            try {
                Map<String, Map<String, Object>> userContents = null;
                try {
                /* 获取整理后的待发送信息
                userContents 订阅用户对应的发送信息Map<发送目标，如邮件地址、电话号码等, Map<发送类型, 根据类型不同存储的不同类型的值>> */
                    userContents = iMessageBizService.actGetSendMap(msgRecord.getTaskType(), msgRecord.getTransactionId(), msgRecord.getTemplateData(), msgRecord.getFileIds()).getD();
                } catch (MessageMqException e) {
                    logger.error("获取待发送信息错误", e);
                } catch (Exception e) {
                    throw e;
                }
                if (userContents != null) {
                    for (String sourceStr : userContents.keySet()) {
                        Map<String, Object> map = userContents.get(sourceStr);
                        String[] arr = sourceStr.split("\\.");
                        if (arr.length != 2) {

                        }
                        String collectionName = arr[0];
                        String personId = arr[1];
                        BasicDBObject source = mongoTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(personId))), BasicDBObject.class, collectionName);
                        if (source == null || source.isEmpty()) {
                            continue;
                        }
                        String loginUserId = source.getString("loginUserId");
                        String loginUserName = null;
                        if (loginUserId != null) {
                            LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(loginUserId).getD();
                            if (loginUserBean != null) {
                                loginUserName = loginUserBean.getUsername();
                            }
                        }
                        String email = source.getString("email");
                        String cell = source.getString("cell");
                        String wx_openid = source.getString("wx_openid");
                        for (String key : map.keySet()) {
                            Object value = map.get(key);
                            if (value != null && !"".equals(value)) {
                                if (Channels.WEBSOCKET.equals(key)) {
                                    if (msgRecord.getToList() == null || canSend(msgRecord.getToList(), sourceStr)) {
                                        iAmqpBizService.actSendWebsocketMq(loginUserName, value.toString());
                                    }
                                }
                                if (Channels.EMAIL.equals(key)) {
                                    if (msgRecord.getToList() == null || canSend(msgRecord.getToList(), sourceStr)) {
                                        ResultBean<String> resultBean = iMessageBizService.actSendEmail(email, value instanceof Map ? (Map) value : null);
                                        msgRecord.getMessageLogId().add(resultBean.getD());
                                    }
                                }
                                if (Channels.WECHAT.equals(key)) {
                                    if (msgRecord.getToList() == null || canSend(msgRecord.getToList(), sourceStr)) {

                                    }
                                }
                                if (Channels.PAD.equals(key)) {
                                    if (msgRecord.getToList() == null || canSend(msgRecord.getToList(), sourceStr)) {
                                        if (value instanceof Map) {
                                            MessageLogBean messageLog = new MessageLogBean();
                                            Map putCtrl = msgRecord.getPushCtrlMap();
                                            PushDataBean pushData = new PushDataBean();
                                            pushData.setTicker(((Map) value).get("subject").toString());
                                            pushData.setTitle(((Map) value).get("subject").toString());
                                            pushData.setText(((Map) value).get("body").toString());
                                            // push控制字段
                                            pushData.setExtraFields((Map<String, String>) putCtrl.get("extraFields"));
                                            switch (Integer.parseInt(putCtrl.get("afterOpenAction").toString())) {
                                                case 1:
                                                    pushData.setAfterOpenAction(PushDataBean.go_app);
                                                    break;
                                                case 2:
                                                    pushData.setAfterOpenAction(PushDataBean.go_url);
                                                    pushData.setUrl(putCtrl.get("go_url").toString());
                                                    break;
                                                case 3:
                                                    pushData.setAfterOpenAction(PushDataBean.go_activity);
                                                    pushData.setActivity(putCtrl.get("go_activity").toString());
                                                    break;
                                                case 4:
                                                    pushData.setAfterOpenAction(PushDataBean.go_custom);
                                                    pushData.setCustomer(putCtrl.get("go_custom").toString());
                                                    break;
                                            }
                                            ResultBean resultBean = iPushDataBizService.actPushData(loginUserName, pushData);
                                            Map contentMap = new HashMap<>();
                                            contentMap.put("title", ((Map) value).get("subject").toString());
                                            contentMap.put("text", ((Map) value).get("body").toString());
                                            messageLog.setContent(contentMap);
                                            messageLog.setTo(loginUserName);
                                            if (resultBean.isSucceed() && !"A015_DECLARATION_CREATE_SURVEY".equals(msgRecord.getTaskType())) {
                                                NoticeBean noticeBean = new NoticeBean();
                                                noticeBean.setTitle((String) contentMap.get("title"));
                                                noticeBean.setContent((String) contentMap.get("text"));
                                                noticeBean.setType(NoticeBean.TYPE_3);
                                                noticeBean.setSendType(Channels.PAD);
                                                noticeBean.setAfterOpenAction(PushDataBean.go_activity);
                                                noticeBean.setGoActivity(putCtrl.get("go_activity").toString());
                                                Map extraFields = (Map) putCtrl.get("extraFields");
                                                noticeBean.setBillId(extraFields.get("billId").toString());
                                                noticeBean.setBusinessType(extraFields.get("businessType").toString());
                                                if (extraFields.containsKey("id")) {
                                                    noticeBean.setOrderId(extraFields.get("id").toString());
                                                }
                                                Set set = new HashSet<>();
                                                set.add(source.getString("_id"));
                                                noticeBean.setLoginUserNames(set);
                                                noticeBean.setSendTime(DateTimeUtils.getCreateTime());
                                                noticeBean.setStatus(3);
                                                iMessageBizService.actSaveNotice(noticeBean);
                                                messageLog.setResult(MessageLogBean.SUCCESS);
                                            } else {
                                                messageLog.setResult(MessageLogBean.FAILD);
                                            }
                                            messageLog = iMessageBizService.actSaveMessageLog(messageLog).getD();
                                            msgRecord.getMessageLogId().add(messageLog.getId());
                                        }
                                    }
                                }
                                if (Channels.SMS.equals(key)) {
                                    if (msgRecord.getToList() == null || canSend(msgRecord.getToList(), sourceStr)) {
                                        List cells = new ArrayList<>();
                                        cells.add(cell);
                                        ResultBean<String> resultBean = iMessageBizService.actSendSMS(cells, value.toString());
                                        if (resultBean.failed()) {
                                            logger.error("短信发送失败:" + resultBean.getM());
                                        }
                                        msgRecord.getMessageLogId().add(resultBean.getD());
                                    }
                                }
                            }
                        }
                    }
                }
                msgRecord.setStatus(MsgRecordBean.STATUS_SUCCESS);
                msgRecord.setResult("success");
            } catch (Exception e) {
                msgRecord.setStatus(MsgRecordBean.STATUS_FAILD);
                msgRecord.setResult(e.getMessage());
                logger.error("send message error, record id:" + msgRecord.getId(), e);
            }
            iAmqpBizService.actSaveMsgRecord(msgRecord);
        } else {
            msgRecord.setStatus(MsgRecordBean.STATUS_EXECUTE);
            NoticeBean noticeBean = iMessageBizService.actGetNotice(msgRecord.getNoticeId()).getD();
            try {
                if (noticeBean != null) {
                    noticeBean.setStatus(2);
                    String type = noticeBean.getType(); // 发送类型 通知、公告、消息
                    List<String> loginUserNames = new ArrayList<>();
                    if (NoticeBean.TYPE_1.equals(type)) { // 通知 ，需要查询指定的发送人
                        if (noticeBean.getFromGroup() == 1) {
                            for (String employeeId : noticeBean.getLoginUserNames()) {
                                EmployeeBean employeeBean = iOrgBizService.actGetEmployee(employeeId).getD();
                                if (employeeBean != null) {
                                    LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(employeeBean.getLoginUserId()).getD();
                                    if (loginUserBean != null) {
                                        loginUserNames.add(loginUserBean.getUsername());
                                    }
                                }
                            }
                        } else if (noticeBean.getFromGroup() == 2) {
                            String groupId = noticeBean.getGroupId();
                            List<LoginUserBean> loginUsers = iAuthenticationBizService.actGetLoginUserByUserRoleIds(groupId).getD();
                            for (LoginUserBean loginUserBean : loginUsers) {
                                loginUserNames.add(loginUserBean.getUsername());
                            }
                        } else if (noticeBean.getFromGroup() == 3) {
                            String orgId = noticeBean.getOrgId();
                            List<EmployeeBean> employeeBeans = iOrgBizService.actGetOrgEmployees(orgId).getD();
                            for (EmployeeBean e : employeeBeans) {
                                if (e.getLoginUserId() != null) {
                                    LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(e.getLoginUserId()).getD();
                                    if (loginUserBean != null) {
                                        loginUserNames.add(loginUserBean.getUsername());
                                    }
                                }
                            }
                        }
                    }
                    if (NoticeBean.TYPE_2.equals(type)) { // 公告 全员发送
                        List<LoginUserBean> loginUserBeans = iAuthenticationBizService.actLookupLoginUser().getD();
                        if (loginUserBeans != null && loginUserBeans.size() > 0) {
                            loginUserBeans.forEach(bean -> {
                                loginUserNames.add(bean.getUsername());
                            });
                        }
                    }
                    this.sendNotice(loginUserNames, noticeBean, msgRecord);
                }
                msgRecord.setStatus(MsgRecordBean.STATUS_SUCCESS);
                msgRecord.setResult("success");
            } catch (Exception e) {
                msgRecord.setStatus(MsgRecordBean.STATUS_FAILD);
                noticeBean.setStatus(4);
                msgRecord.setResult(e.getMessage());
                logger.error("send message error, record id:" + msgRecord.getId(), e);
            }
            iMessageBizService.actSaveNotice(noticeBean);
            iAmqpBizService.actSaveMsgRecord(msgRecord);
        }
    }

    /**
     * 发送通知功能，现在只支持短信、pad推送
     *
     * @param loginUserNames
     * @param noticeBean
     * @param msgRecord
     */
    private void sendNotice(List<String> loginUserNames, NoticeBean noticeBean, MsgRecordBean msgRecord) {
        String sendType = noticeBean.getSendType();
        if (Channels.SMS.equals(sendType)) {
            ResultBean<String> resultBean = iMessageBizService.actSendSMS(loginUserNames, noticeBean.getContent());
            if (resultBean.failed()) {
                logger.error("短信发送失败:" + resultBean.getM());
            }
            msgRecord.getMessageLogId().add(resultBean.getD());
        }
        if (Channels.PAD.equals(sendType)) {
            MessageLogBean messageLog = new MessageLogBean();
            PushDataBean pushData = new PushDataBean();
            pushData.setTicker(noticeBean.getTitle());
            pushData.setTitle(noticeBean.getTitle());
            pushData.setText(noticeBean.getContent());
            pushData.setAfterOpenAction(PushDataBean.go_activity);
            pushData.setActivity("com.fzfq.fuzecredit.home.activity.MainActivity");
            Map<String, String> extraFields = new HashMap<>();
            extraFields.put("messageType", noticeBean.getType());
            pushData.setExtraFields(extraFields);
            ResultBean resultBean = iPushDataBizService.actPushDatas(loginUserNames, pushData);
            Map contentMap = new HashMap<>();
            contentMap.put("title", noticeBean.getTitle());
            contentMap.put("text", noticeBean.getContent());
            messageLog.setContent(contentMap);
            messageLog.setTo(loginUserNames.toString());
            if (resultBean.isSucceed()) {
                noticeBean.setStatus(3);
                messageLog.setResult(MessageLogBean.SUCCESS);
            } else {
                noticeBean.setStatus(4);
                messageLog.setResult(MessageLogBean.FAILD);
            }
            messageLog = iMessageBizService.actSaveMessageLog(messageLog).getD();
            msgRecord.getMessageLogId().add(messageLog.getId());
        }
    }


    private boolean canSend(Map<String, List<String>> toList, String sourceStr) {
        boolean flag = false;
        for (String type : toList.keySet()) {
            if (toList.get(type).contains(sourceStr.split("\\.")[1])) {
                flag = true;
            }
        }
        return flag;
    }

}
