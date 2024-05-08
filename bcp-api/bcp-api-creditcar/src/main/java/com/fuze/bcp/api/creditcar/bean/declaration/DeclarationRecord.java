package com.fuze.bcp.api.creditcar.bean.declaration;


import com.fuze.bcp.api.bd.bean.HistoryRecord;
import lombok.Data;

/**
 * 报批历史记录
 */
@Data
public class DeclarationRecord extends HistoryRecord {

    //mq发送消息记录ID   MsgRecordBeanId
    private String resultId;

    /**
     *  邮件发送的时间
     */
    private String emailTime;

    /**
     * 邮件是否发送成功
     */
    private Boolean emailSuccess;

    /**
     * 银行批复结果
     */
    private DeclarationResult declarationResult = new DeclarationResult();

}
