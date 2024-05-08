package com.fuze.bcp.api.creditcar.bean.bankcardapply;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.creditcar.bean.APIBillListBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import lombok.Data;

/**
 * 制卡单
 */
@Data
public class BankCardApplyListBean extends APIBillListBean {
    /**
     * 客户姓名
     */
    private String customerName = null;

    /**
     * 身份证号
     */
    private String customerIdentifyNo = null;

    /**
     * 客户手机号
     */
    private String customerCell = null;

    /**
     * 卡业务状态：初始状态
     */
    public Integer status = BankCardApplyBean.BKSTATUS_INIT;

    /**
     * 银行申请制卡时间
     */
    private String applyTime ;

    /**
     * 卡号
     */
    @JsonProperty("accountNumber")
    private String cardNo = null;

    /**
     * 有效期
     */
    @JsonProperty("validityPeriod")
    private String expireDate= null;

    /**
     * CVV/校验码
     */
    @JsonProperty("CVVCode")
    private String cvv = null;

    /**
     * 初始密码
     * TODO 加密该字段
     */
    @JsonProperty("password")
    private String initPassword = null;

    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A011";
    }

}
