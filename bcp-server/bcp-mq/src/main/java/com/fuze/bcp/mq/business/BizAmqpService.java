package com.fuze.bcp.mq.business;

import com.fuze.bcp.api.mq.bean.*;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.msg.bean.NoticeBean;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.mq.domain.DynamicDescribe;
import com.fuze.bcp.mq.domain.MsgRecord;
import com.fuze.bcp.mq.domain.TaskDescribe;
import com.fuze.bcp.mq.domain.TaskRecord;
import com.fuze.bcp.mq.service.IDynamicDescribeService;
import com.fuze.bcp.mq.service.IMsgRecordService;
import com.fuze.bcp.mq.service.ITaskDescribeService;
import com.fuze.bcp.mq.service.ITaskRecordService;
import com.fuze.bcp.mq.service.sender.ISenderService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by CJ on 2017/7/3.
 */
@Service
public class BizAmqpService implements IAmqpBizService {

    @Autowired
    ISenderService senderService;

    @Autowired
    ITaskDescribeService iTaskDescribeService;

    @Autowired
    IMsgRecordService iMsgRecordService;

    @Autowired
    IDynamicDescribeService iDynamicDescribeService;

    @Autowired
    ITaskRecordService iTaskRecordService;

    @Autowired
    IMessageBizService iMessageBizService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean actSendWebsocketMq(String userId, String content) {
        senderService.sendWebsocketMsg(userId, content);
        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<Map> actSendMq(String taskType, Object[] params, MsgRecordBean msgRecordBean) {
        Map result = new HashMap<>();
        result.put("business", this.actSendBusinessMsg(taskType, params).getD());
        result.put("message", this.actSendMessageMsg(msgRecordBean).getD());
        return ResultBean.getSucceed().setD(result);
    }

    @Override
    public ResultBean<MsgRecordBean> actSendMessageMsg(MsgRecordBean msgRecordBean) {
        MsgRecord m = mappingService.map(msgRecordBean, MsgRecord.class);
        senderService.sendMessageMsg(msgRecordBean);
        iMsgRecordService.save(m);
        return ResultBean.getSucceed().setD(mappingService.map(m, MsgRecordBean.class));
    }

    @Override
    public ResultBean<List<TaskDescribeBean>> actSendBusinessMsg(String taskType, Object[] params) {
        Map<String, String> map = new HashMap<>();
        ResultBean<List<TaskDescribeBean>> resultBean = actGetTaskDescribeByType(taskType);
        List<TaskDescribeBean> taskDescribeBeans = resultBean.getD();
        if (taskDescribeBeans == null) {
            return ResultBean.getSucceed();
        }
        for (TaskDescribeBean taskDescribeBean : taskDescribeBeans) {
            TaskRecord taskRecord = new TaskRecord(taskDescribeBean, params);
            taskRecord.setResult("init");
            taskRecord.setStartDate(DateTimeUtils.getSimpleDateFormat().format(new Date(System.currentTimeMillis())));
            taskRecord = iTaskRecordService.save(taskRecord);
            map.put(taskRecord.getId(), taskDescribeBean.getId());
        }
        if (!map.isEmpty()) {
            senderService.sendBusinessMsg(map);
        }
        return ResultBean.getSucceed().setD(taskDescribeBeans);
    }

    @Override
    public ResultBean actSendLogMsg(BusinessLogBean businessLogBean) {
        return null;
    }

    @Override
    public ResultBean<NoticeBean> actSendNotice(String noticeId) {
        MsgRecordBean msgRecordBean = new MsgRecordBean(noticeId);
        NoticeBean noticeBean = iMessageBizService.actGetNotice(noticeId).getD();
        if (noticeBean != null) {
            noticeBean.setSendTime(DateTimeUtils.getCreateTime());
            iMessageBizService.actSaveNotice(noticeBean);
            MsgRecord m = mappingService.map(msgRecordBean, MsgRecord.class);
            senderService.sendMessageMsg(msgRecordBean);
            iMsgRecordService.save(m);
            return ResultBean.getSucceed().setD(noticeBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public void actSendWeb3jMsg(Map<String,Object> map) {
        senderService.sendWeb3jMsg(map);
    }

    @Override
    public ResultBean<TaskDescribeBean> actSaveTaskDescribe(TaskDescribeBean taskDescribeBean) {
        TaskDescribe taskDescribe = iTaskDescribeService.getAvailableOne(taskDescribeBean.getId());
        if (taskDescribe == null) { // 新建
            taskDescribe = new TaskDescribe(taskDescribeBean);
        } else { // 修改
            taskDescribe.setType(taskDescribeBean.getType());
            taskDescribe.setName(taskDescribeBean.getName());
            taskDescribe.setCode(taskDescribeBean.getCode());
            taskDescribe.setMethodName(taskDescribeBean.getMethodName());
        }
        taskDescribe = iTaskDescribeService.save(taskDescribe);
        return ResultBean.getSucceed().setD(mappingService.map(taskDescribe, TaskDescribeBean.class));
    }

    @Override
    public ResultBean<TaskDescribeBean> actDeleteTaskDescribe(String id) {
        List<DynamicDescribe> dynamicDescribes = iDynamicDescribeService.findByTaskId(id);
        if (dynamicDescribes != null && dynamicDescribes.size() > 0) {
            iDynamicDescribeService.deleteReal(dynamicDescribes);
        }
        return ResultBean.getSucceed().setD(mappingService.map(iTaskDescribeService.deleteReal(id), TaskDescribeBean.class));
    }

    @Override
    public ResultBean<DynamicDescribeBean> actDeleteDynamicDescribe(String id) {
        TaskDescribe taskDescribe = iTaskDescribeService.getByDynamicDescribeId(id);
        if (taskDescribe != null) {
            taskDescribe.setDynamicDescribeId(null);
            iTaskDescribeService.save(taskDescribe);
        }
        DynamicDescribe parent = iDynamicDescribeService.findByNeededResource(id);
        if (parent != null && parent.getNeededResourceId() != null && parent.getNeededResourceId().size() > 0) {
            List<String> needResources = parent.getNeededResourceId();
            needResources.remove(id);
            parent.setNeededResourceId(needResources);
            iDynamicDescribeService.save(parent);
        }
        deleteDynamicDescribe(id);
        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<DynamicDescribeBean> actSaveDynamicDescribe(DynamicDescribeBean dynamicDescribeBean, String taskDescId, String parentId) {
        String dynamicDescribeBeanId = saveDynamicDescribe(dynamicDescribeBean, taskDescId);
        if (parentId != null) {
            DynamicDescribe dynamicDescribe = iDynamicDescribeService.getAvailableOne(parentId);
            List<String> resources = new ArrayList();
            if (dynamicDescribe.getNeededResourceId() != null) {
                resources = dynamicDescribe.getNeededResourceId();
            }
            if (!resources.contains(dynamicDescribeBeanId)) {
                resources.add(dynamicDescribeBeanId);
            }
            dynamicDescribe.setNeededResourceId(resources);
            iDynamicDescribeService.save(dynamicDescribe);
        } else {
            TaskDescribe taskDescribe = iTaskDescribeService.getAvailableOne(taskDescId);
            taskDescribe.setDynamicDescribeId(dynamicDescribeBeanId);
            iTaskDescribeService.save(taskDescribe);
        }
        dynamicDescribeBean.setId(dynamicDescribeBeanId);
        return ResultBean.getSucceed().setD(dynamicDescribeBean);
    }

    @Override
    public ResultBean<DataPageBean<TaskDescribeBean>> actGetTaskDescribes(Integer currentPage, Boolean flag) {
        Page<TaskDescribe> taskDescribes = iTaskDescribeService.getAllOrderByType(currentPage);
        Page<TaskDescribeBean> taskDescribeBeans = taskDescribes.map(new Converter<TaskDescribe, TaskDescribeBean>() {
            public TaskDescribeBean convert(TaskDescribe source) {
                String dynamicDescribeId = source.getDynamicDescribeId();
                TaskDescribeBean t = mappingService.map(source, TaskDescribeBean.class);
                if (flag) {
                    t.setDynamicDescribeBean(null);
                } else {
                    t.setDynamicDescribeBean(getDynamicDescribe(dynamicDescribeId));
                }
                return t;
            }
        });
        DataPageBean<TaskDescribeBean> destination = new DataPageBean<TaskDescribeBean>();
        destination.setPageSize(taskDescribeBeans.getSize());
        destination.setTotalCount(taskDescribeBeans.getTotalElements());
        destination.setTotalPages(taskDescribeBeans.getTotalPages());
        destination.setCurrentPage(taskDescribeBeans.getNumber());
        for (TaskDescribeBean t : taskDescribeBeans.getContent()) {
            destination.getResult().add(t);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<List<DynamicDescribeBean>> actGetDynamicDescribeBeansByTaskDescribeId(String taskdescribeId) {
        TaskDescribe taskDescribe = iTaskDescribeService.getAvailableOne(taskdescribeId);
        if (taskDescribe != null) {
            return ResultBean.getSucceed().setD(getDynamicDescribe(taskDescribe.getDynamicDescribeId()));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<TaskDescribeBean>> actGetTaskDescribeByType(String type) {
        List<TaskDescribe> taskDescribes = iTaskDescribeService.getByDataStatusAndType(type);
        if (taskDescribes == null || taskDescribes.size() == 0) {
            return ResultBean.getSucceed();
        }
        List<TaskDescribeBean> taskDescribeBeans = new ArrayList<>();
        for (TaskDescribe taskDescribe : taskDescribes) {
            String dynamicDescribeId = taskDescribe.getDynamicDescribeId();
            TaskDescribeBean t = mappingService.map(taskDescribe, TaskDescribeBean.class);
            t.setDynamicDescribeBean(getDynamicDescribe(dynamicDescribeId));
            taskDescribeBeans.add(t);
        }
        return ResultBean.getSucceed().setD(taskDescribeBeans);
    }

    @Override
    public ResultBean<TaskDescribeBean> actGetTaskDescribe(String id) {
        TaskDescribe taskDescribe = iTaskDescribeService.getOne(id);
        if (taskDescribe == null) {
            return ResultBean.getFailed();
        }
        String dynamicDescribeId = taskDescribe.getDynamicDescribeId();
        TaskDescribeBean t = mappingService.map(taskDescribe, TaskDescribeBean.class);
        t.setDynamicDescribeBean(getDynamicDescribe(dynamicDescribeId));
        return ResultBean.getSucceed().setD(t);
    }

    @Override
    public ResultBean<DataPageBean<TaskRecordBean>> actGetTaskRecords(Integer currentPage) {
        Page<TaskRecord> taskRecords = iTaskRecordService.getAll(currentPage);
        Page<TaskRecordBean> taskRecordBeen = taskRecords.map(new Converter<TaskRecord, TaskRecordBean>() {
            public TaskRecordBean convert(TaskRecord source) {
                TaskRecordBean t = mappingService.map(source, TaskRecordBean.class);
                t.setParams(source.getParams());
                return t;
            }
        });
        DataPageBean<TaskRecordBean> destination = new DataPageBean<TaskRecordBean>();
        destination.setPageSize(taskRecordBeen.getSize());
        destination.setTotalCount(taskRecordBeen.getTotalElements());
        destination.setTotalPages(taskRecordBeen.getTotalPages());
        destination.setCurrentPage(taskRecordBeen.getNumber());
        for (TaskRecordBean t : taskRecordBeen.getContent()) {
            destination.getResult().add(t);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<TaskRecordBean> actGetTaskRecord(String id) {
        TaskRecord taskRecord = iTaskRecordService.getOne(id);
        TaskRecordBean taskRecordBean = mappingService.map(taskRecord, TaskRecordBean.class);
        taskRecordBean.setParams(taskRecord.getParams());
        return ResultBean.getSucceed().setD(taskRecordBean);
    }

    @Override
    public ResultBean<TaskRecordBean> actSaveTaskRecord(TaskRecordBean taskRecordBean) {
        TaskRecord taskRecord = mappingService.map(taskRecordBean, TaskRecord.class);
        taskRecord = iTaskRecordService.save(taskRecord);
        return ResultBean.getSucceed().setD(mappingService.map(taskRecord, TaskRecordBean.class));
    }

    @Override
    public ResultBean<List<TaskDescribeLookupBean>> actLookupTasDesc() {
        List<TaskDescribe> taskDescribes = iTaskDescribeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(taskDescribes, TaskDescribeLookupBean.class));
    }

    @Override
    public ResultBean<MsgRecordBean> actGetMsgRecord(String id) {
        MsgRecord msgRecord = iMsgRecordService.getOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(msgRecord, MsgRecordBean.class));
    }

    @Override
    public ResultBean<List<MsgRecordBean>> actGetMsgRecords(List<String> ids) {
        List<MsgRecord> msgRecordList = iMsgRecordService.getAvaliableList(ids);
        return ResultBean.getSucceed().setD(mappingService.map(msgRecordList, MsgRecordBean.class));
    }

    @Override
    public ResultBean<MsgRecordBean> actSaveMsgRecord(MsgRecordBean msgRecordBean) {
        MsgRecord msgRecord = mappingService.map(msgRecordBean, MsgRecord.class);
        msgRecord = iMsgRecordService.save(msgRecord);
        return ResultBean.getSucceed().setD(mappingService.map(msgRecord, MsgRecordBean.class));
    }

    private void deleteDynamicDescribe(String dynamicDescribeId) {
        DynamicDescribe dynamicDescribe = iDynamicDescribeService.getAvailableOne(dynamicDescribeId);
        if (dynamicDescribe.getNeededResourceId() != null && dynamicDescribe.getNeededResourceId().size() > 0) {
            for (int i = 0; i < dynamicDescribe.getNeededResourceId().size(); i++) {
                deleteDynamicDescribe(dynamicDescribe.getNeededResourceId().get(i));
            }
        }
        iDynamicDescribeService.deleteReal(dynamicDescribeId);
    }

    private String saveDynamicDescribe(DynamicDescribeBean dynamicDescribeBean, String taskId) {
        List childIds = new ArrayList<>();
        if (dynamicDescribeBean.getNeededResource() != null && dynamicDescribeBean.getNeededResource().size() > 0) {
            for (int i = 0; i < dynamicDescribeBean.getNeededResource().size(); i++) {
                String id = saveDynamicDescribe(dynamicDescribeBean.getNeededResource().get(i), taskId);
                childIds.add(id);
            }
        }
        DynamicDescribe dynamicDescribe = mappingService.map(dynamicDescribeBean, DynamicDescribe.class);
        dynamicDescribe.setNeededResourceId(childIds);
        dynamicDescribe.setTaskId(taskId);
        dynamicDescribe = iDynamicDescribeService.save(dynamicDescribe);
        return dynamicDescribe.getId();
    }

    private DynamicDescribeBean getDynamicDescribe(String dynamicDescribeId) {
        List<DynamicDescribeBean> childs = new ArrayList<>();
        if (dynamicDescribeId != null) {
            DynamicDescribe dynamicDescribe = iDynamicDescribeService.getAvailableOne(dynamicDescribeId);
            if (dynamicDescribe.getNeededResourceId() != null && dynamicDescribe.getNeededResourceId().size() > 0) {
                for (int i = 0; i < dynamicDescribe.getNeededResourceId().size(); i++) {
                    DynamicDescribeBean dynamicDescribeBean = getDynamicDescribe(dynamicDescribe.getNeededResourceId().get(i));
                    childs.add(dynamicDescribeBean);
                }
            }
            DynamicDescribeBean dynamicDescribeBean = mappingService.map(dynamicDescribe, DynamicDescribeBean.class);
            dynamicDescribeBean.setNeededResource(childs);
            return dynamicDescribeBean;
        }
        return null;
    }


}
