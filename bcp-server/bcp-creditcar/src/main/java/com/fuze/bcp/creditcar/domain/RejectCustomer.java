package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.customer.bean.CustomerRelation;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 被拒绝客户信息
 * Created by sean on 16/10/10.
 */
@Document(collection = "so_reject_customer")
@Data
public class RejectCustomer extends MongoBaseEntity {

    //  身份证号码
    private String identifyNo;

    //  交易ID
    private String transactionId;

    //  客户ID
    private String customerId;

    //  身份：贷款人（0）、配偶（1）、指标人（2）
    private List<CustomerRelation> customerRelations;

    //  拒绝原因
    private String rejectReason;

    //  审批人ID
    private String approveCustomerId;

    //  审批时间
    private String approveDate;

}
