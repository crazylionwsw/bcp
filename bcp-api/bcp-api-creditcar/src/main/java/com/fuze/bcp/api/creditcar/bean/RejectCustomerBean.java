package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.api.customer.bean.CustomerRelation;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.List;

/**
 * 被拒绝客户信息
 * Created by zqw on 2017-12-18.
  */
@Data
public class RejectCustomerBean extends APIBaseBean {

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
