package com.fuze.bcp.bd.domain;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 资金提供方-员工信息
 * Created by sean on 16/10/10.
 */
@Document(collection = "bd_cashsourceemployee")
public class CashSourceEmployee extends Employee{

    /**
     * 微信openid
     */

    /**
     * 所属资金机构
     */
    private String cashSourceId;

    public String getCashSourceId() {
        return cashSourceId;
    }

    public void setCashSourceId(String cashSourceId) {
        this.cashSourceId = cashSourceId;
    }
}
