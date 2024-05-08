package com.fuze.bcp.api.creditcar.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.LoanSubmissionBean;
import com.fuze.bcp.api.customer.bean.PadCustomerCarBean;
import lombok.Data;

/**
 * 资质审查提交单（PAD）
 */
@Data
public class DemandSubmissionBean extends BillSubmissionBean {

    /**
     * 是否需要反担保人
     */
    private Integer needCounterGuarantor = 0;

    /**
     * 贷款主体信息，同样的客户基本信息
     */
    private CustomerBean creditMaster = null;

    /**
     * 抵押人 （指标人）
     */
    private CustomerBean pledgeCustomer = null;

    /**
     * 配偶
     */
    private CustomerBean mateCustomer = null;

    /**
     * 贷款主体与客户之间关系
     */
    private String relation;

    /**
     * 选购车辆信息
     */
    private PadCustomerCarBean customerCar = null;

    /**
     * 拟贷款产品
     */
    private String creditProductId = null;

    /**
     * 借款需求
     */
    private LoanSubmissionBean customerLoan = null;

    /**
     * 4S店销售人员
     */
    private String  dealerEmployeeId = null;

    /**
     * 二手车首次购车日期
     */
    private String firstPurchaseDate = null;

    /**
     * 评估单id
     */
    private String vehicleEvaluateInfoId = null;

}
