package com.fuze.bcp.api.cardealer.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.ApproveStatus;
import lombok.Data;

/**
 * 装修公司
 */
@Data
@MongoEntity(entityName = "bd_channel")
public class DecorateBean extends ChannelBaseBean implements ApproveStatus {

    /**
     * 审核状态
     */
    private Integer approveStatus = APPROVE_INIT;

    /**
     * 审批流的ID
     */
    private String activitiId = null;

    /**
     * 单据编码
     * @return
     */
    public String getBillTypeCode() {
        return "H001";
    }
}
