package com.fuze.bcp.api.msg.service;

import com.fuze.bcp.api.msg.bean.*;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.exception.MessageMqException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/7/20.
 */
public interface IMessageBizService {


    /**
     * 根据订阅信息发送消息
     *
     * @param businessType
     * @param transactionId
     * @param map
     * @return
     */
    ResultBean<Map<String, Map<String, Object>>> actGetSendMap(String businessType, String transactionId, Map map, List<String> fileIds) throws MessageMqException;

    ResultBean<DataPageBean<MessageTemplateBean>> actGetMessageTemplates(Integer currentPage);

    ResultBean<MessageTemplateBean> actGetMessageTemplate(String templateId);

    ResultBean<String> actGetMessageFromTemplate(String templateId, Map params) throws IOException;

    /**
     * 添加消息类型
     *
     * @param messageTemplateBean
     * @return
     */
    ResultBean<MessageTemplateBean> actSaveMessageTemplate(MessageTemplateBean messageTemplateBean);

    /**
     * 清除一个消息类型
     *
     * @param templateId
     * @return
     */
    ResultBean<MessageTemplateBean> actDeleteMessageTemplate(String templateId);

    /**
     * 取得消息类型列表
     *
     * @return
     */
    ResultBean<List<MessageTemplateBean>> actGetTemplateByBusinessType(String messageType);


    /**
     * 保存发送信息
     *
     * @param messageLogBean
     * @return
     */
    ResultBean<MessageLogBean> actSaveMessageLog(MessageLogBean messageLogBean);

    /**
     * 获取历史记录
     *
     * @return
     */
    ResultBean<List<MessageLogBean>> actGetMessageLogs(List<String> logIds);

    ResultBean<MessageLogBean> actGetMessageLog(String id);

    /**
     * 发送多条短信
     *
     * @param cells
     * @param content
     * @return
     */
    ResultBean<String> actSendSMS(List<String> cells, String content);

    /**
     * 发送邮件
     */
    ResultBean<String> actSendEmail(String to, Map emailInfo);

    ResultBean<String> actSendEmails(List<String> tos, Map emailInfo);

    ResultBean<FeedbackBean> actSaveFeedBack(FeedbackBean feedbackBean);

    ResultBean<DataPageBean<FeedbackBean>> actGetFeedBacks(Integer currentPage);

//    ResultBean<DataPageBean<FeedbackBean>> actGetUserFeedBacks(Integer currentPage, String loginUserId);

//    ResultBean<List<FeedbackBean>> actDetailFeedback(String rootId);

    ResultBean<FeedbackBean> actDeleteFeedback(String id);

    ResultBean<NoticeBean> actGetNotice(String id);

    ResultBean<DataPageBean<NoticeBean>> actGetNotices(Integer currentPage);

    ResultBean<NoticeBean> actSaveNotice(NoticeBean noticeBean);

    ResultBean<NoticeBean> actDeleteNotice(String id);

    //订阅维护查询
    ResultBean<DataPageBean<MessageSubScribeBean>> searchMessageSubScribes(Integer currentPage,MessageSubScribeBean messageSubScribeBean );

    ResultBean<DataPageBean<NoticeLookupBean>> actGetMyNoticeForPad(Integer currentPage, Integer pageSize, String type, String sendType, String loginUserName);

}
