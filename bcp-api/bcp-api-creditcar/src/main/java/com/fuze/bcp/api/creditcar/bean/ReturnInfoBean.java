package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;

import java.util.List;

/**
 * 资料归还单
 */
@Data
public class ReturnInfoBean extends APICarBillBean {

    /**
     * 取件代理人名称
     */
    private String agentName = null;

    /**
     * 取件代理人手机
     */
    private String agentCell = null;

    /**
     * 取件代理人身份证号码
     */
    private String agentIdentifyNo = null;

    /**
     * 取件验证码
     */
    private String validateCode = null;

    /**
     * 预约取件时间
     */
    private String bookTime = null;

    /**
     * 归还时间
     */
    private String returnTime = null;

    /**
     * 归还的档案资料
     */
    private List<String> customerImageTypeCodes;

    /**
     * 子类需要定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A009";
    }
}
