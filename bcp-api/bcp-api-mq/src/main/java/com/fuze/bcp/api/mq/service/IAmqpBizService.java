package com.fuze.bcp.api.mq.service;

import com.fuze.bcp.api.mq.bean.*;
import com.fuze.bcp.api.msg.bean.NoticeBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/7/3.
 */
public interface IAmqpBizService {

    ResultBean<Map> actSendMq(String taskType, Object[] params, MsgRecordBean msgRecordBean);

    ResultBean actSendWebsocketMq(String userId, String content);

    ResultBean actSendBusinessMsg(String taskType, Object[] params);

    ResultBean actSendMessageMsg(MsgRecordBean msgRecordBean);

    ResultBean actSendLogMsg(BusinessLogBean businessLogBean);

    ResultBean<NoticeBean> actSendNotice(String noticeId);

    void actSendWeb3jMsg(Map<String,Object> map);

    ResultBean<TaskDescribeBean> actSaveTaskDescribe(TaskDescribeBean taskDescribeBean);

    ResultBean<DynamicDescribeBean> actSaveDynamicDescribe(DynamicDescribeBean dynamicDescribeBean, String taskDescId, String parentId);

    ResultBean<DynamicDescribeBean> actDeleteDynamicDescribe(String id);

    ResultBean<DataPageBean<TaskDescribeBean>> actGetTaskDescribes(Integer currentPage, Boolean flag);

    ResultBean<List<DynamicDescribeBean>> actGetDynamicDescribeBeansByTaskDescribeId(String taskdescribeId);

    ResultBean<TaskDescribeBean> actDeleteTaskDescribe(String id);

    ResultBean<List<TaskDescribeBean>> actGetTaskDescribeByType(String type);

    ResultBean<TaskDescribeBean> actGetTaskDescribe(String id);

    ResultBean<DataPageBean<TaskRecordBean>> actGetTaskRecords(Integer currentPage);

    ResultBean<TaskRecordBean> actGetTaskRecord(String id);

    ResultBean<TaskRecordBean> actSaveTaskRecord(TaskRecordBean taskRecordBean);

    ResultBean<List<TaskDescribeLookupBean>> actLookupTasDesc();

    ResultBean<MsgRecordBean> actGetMsgRecord(String id);

    ResultBean<List<MsgRecordBean>> actGetMsgRecords(List<String> ids);

    ResultBean<MsgRecordBean> actSaveMsgRecord(MsgRecordBean msgRecordBean);

}
