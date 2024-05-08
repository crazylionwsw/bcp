package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.bean.PayAccount;
import com.fuze.bcp.bean.RateType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 资金利率
 */
@Data
@MongoEntity(entityName = "bd_sourcerate")
public class SourceRateBean extends APIBaseDataBean {

    /**
     * 资金来源信息
     */
    private String cashSourceId = null;

    /**
     * 所属的银行账户
     */
    private PayAccount payAccount = null;

    /**
     * 生效日期
     */
    private String startDate = null;

    /**
     * 截止日期，可以为空，如果为空就说明一直生效
     */
    private String endDate = null;

    /**
     * 期数及对应的利率表
     */
    private List<RateType> rateTypes;

    /**
     * 资金总金额
     */
    private Double amount = 0.0;

    /**
     * 允许的还款方式列表
     */
    private List<String> repaymentWayIds = new ArrayList<String>();

    /**
     * 使用条件 (存储条件表达式)
     */
    private List<String> limits = new ArrayList<String>();

}
