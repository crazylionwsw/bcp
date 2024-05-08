package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 资料完善/补全（需要审批流）
 */
@Document(collection = "so_enhancement")
@Data
public class Enhancement extends BaseBillEntity {

    /**
     * 截止时间
     */
    private String dueTime;

    /**
     * 需要补全或完善的档案资料
     */
    private List<String> customerImageTypeCodes;

    /**
     * 关联的档案资料，用于资料补全新上传的档案资料的关联
     */
    private List<String> customerImageIds;

    //  是否允许分期经理删除已有档案，默认不可以删除  0 : 不能删除  1： 可以删除
    private int allowDeleteCustomerImage = 0;

    //  发起人的loginUserId
    private String userId;

    public String getBillTypeCode() {
        return "A013";
    }
}
