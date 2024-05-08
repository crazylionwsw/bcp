package com.fuze.bcp.mq.domain;

import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.utils.DateTimeUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/9/25.
 */
@Document(collection = "mq_msgrecord")
@Data
public class MsgRecord extends MongoBaseEntity {

    public MsgRecord() {
    }

    public MsgRecord(String taskType, String transactionId, Map templateData, List<String> fileIds, Map<String, List<String>> toList) {
        this.taskType = taskType;
        this.transactionId = transactionId;
        this.templateData = templateData;
        this.fileIds = fileIds;
        this.toList = toList;
        this.setStartDate(DateTimeUtils.getCreateTime());
    }

    public MsgRecord(String noticeId) {
        this.bizFlag = false;
        this.noticeId = noticeId;
    }

    /**
     * 是否是业务类消息
     */
    private Boolean bizFlag = true;

    /**
     * 通知ID，非业务状态才可用
     */
    private String noticeId;

    /**
     * 业务事件类型
     */
    private String taskType;

    /**
     * 业务id，为了操作元数据
     */
    private String transactionId;

    /**
     * 发送人
     */
    private String senderId = "admin";

    /**
     * 发送msg的模板内需要单独加入的值
     */
    private Map templateData;

    /**
     * pad推送时需要的控制参数
     */
    private Map pushCtrlMap;

    /**
     * 发送邮件时需要的附件
     */
    private List<String> fileIds;

    /**
     * 订阅只能适用广播形式，此处限制接受广播的人
     */
    private Map<String, List<String>> toList;

    /**
     * 发送结果
     */
    private String result;

    /**
     * 执行状态
     */
    private Integer status = MsgRecordBean.STATUS_INIT;

    private String startDate;

    private List<String> messageLogId = new ArrayList<>();

}
