package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;

import java.util.List;

/**
 * 资料完善/补全单（需要审批流）
 */
@Data
public class EnhancementBean extends APICarBillBean {

    /**
     * 截止时间
     */
    private String dueTime;

    /**
     * 需要补全或完善的档案资料
     */
    private List<String> customerImageTypeCodes;

    //  是否允许分期经理删除已有档案，默认不可以删除  0 : 不能删除  1： 可以删除
    private int allowDeleteCustomerImage = 0;

    //  发起人的loginUserId
    private String userId;

    public String getBillTypeCode() {
        return "A013";
    }
}
