package com.fuze.bcp.customer.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 客户会话中信息卡的信息
 * Created by JZ on 17/04/08.
 */
@Document(collection = "cus_card")
@Data
public class CustomerCard extends MongoBaseEntity {

    /**
     * 客户的ID
     */
    private String customerId = null;

    /**
     * 客户交易ID
     */
    private String customerTransactionId;

    /**
     * 卡号
     */
    private String cardNo = "";

    /**
     * 有效期
     */
    private String expireDate= null;

    /**
     * CVV/校验码
     */
    private String cvv = null;

    /**
     * 初始密码
     * TODO 加密该字段
     */
    private String initPassword = null;

}
