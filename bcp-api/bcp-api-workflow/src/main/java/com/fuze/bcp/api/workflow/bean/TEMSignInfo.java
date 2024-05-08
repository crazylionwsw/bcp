package com.fuze.bcp.api.workflow.bean;

import lombok.Data;

import java.util.Map;

/**
 * Created by Lily on 2018/1/18.
 */
@Data
public class TEMSignInfo extends SignInfo {

    /**
     * 初审
     */
    public final static int AUDIT_START = 0;

    /**
     * 终审
     */
    public final static int AUDIT_END = 1;

    /**
     * 首付比例下限
     */
    private Double downPaymentRatio;

    /**
     * 贷款金额上限
     */
    private Double creditAmount;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 审核角色
     */
    private String auditRole;

    private Map approveVars;

}
