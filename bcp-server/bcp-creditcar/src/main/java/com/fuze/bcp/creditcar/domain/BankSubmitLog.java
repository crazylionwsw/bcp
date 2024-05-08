package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 银行报批信息
 * Created by sean on 2016/11/27.
 *
 */
@Document(collection = "so_bank_submit_log")
@Data
public class BankSubmitLog extends BaseBillEntity {


    /**
     * 签批模板ID
     */
    private String packageTemplateId = null;

    /**
     * 报批是否发送成功
     */
    private Integer submitResult = 0;


    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A007";
    }

}
