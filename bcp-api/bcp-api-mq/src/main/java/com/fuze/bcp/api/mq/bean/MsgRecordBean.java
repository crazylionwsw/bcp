package com.fuze.bcp.api.mq.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发送messageMq消息参数对象
 * Created by CJ on 2017/9/25.
 */

@Data
public class MsgRecordBean extends APIBaseBean implements Serializable {

    public static final Integer STATUS_INIT = 0;

    public static final Integer STATUS_EXECUTE = 1;

    public static final Integer STATUS_SUCCESS = 2;

    public static final Integer STATUS_FAILD = 3;

    public MsgRecordBean() {
    }

    public MsgRecordBean(String taskType, String transactionId, Map templateData, List<String> fileIds, Map<String, List<String>> toList) {
        this.taskType = taskType;
        this.transactionId = transactionId;
        this.templateData = templateData;
        this.fileIds = fileIds;
        this.toList = toList;
        this.bizFlag = true;
    }

    public MsgRecordBean(String noticeId) {
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

    private String taskType;

    private String transactionId;

    /**
     * 发送人
     */
    private String senderId = "admin";

    /**
     * freemarker 模板参数
     */
    private Map templateData;

    /**
     * 友盟 push 控制参数
     */
    private Map pushCtrlMap;

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
    private Integer status;

    private String startDate;

    private List<String> messageLogId = new ArrayList<>();

}
