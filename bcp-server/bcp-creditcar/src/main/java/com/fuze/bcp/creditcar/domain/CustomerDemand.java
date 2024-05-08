package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 客户购物借款需求单
 * Created by sean on 16/10/10.
 */
@Document(collection = "so_customerdemand")
@Data
public class CustomerDemand extends BaseBillEntity {

    /**
     * 是否需要反担保人
     */
    private Integer needCounterGuarantor = 0;

    /**
     * 贷款主体信息，同样的客户基本信息
     */
    private String creditMasterId;

    /**
     * 抵押人 （指标人）
     */
    private String pledgeCustomerId;

    /**
     * 配偶
     */
    private String mateCustomerId;

    /**
     * 贷款主体与客户之间关系
     * 0   本人
     * 1   父子
     * 2   配偶
     * 3   其他
     */
    private String relation;

    /**
     * 选购车辆信息
     */
    private String customerCarId = null;

    /**
     * 拟贷款产品
     */
    private String creditProductId = null;

    /**
     * 借款需求
     */
    private String customerLoanId = null;

    /**
     * 4S店销售人员
     */
    private String dealerEmployeeId;

    /**
     * 二手车首次购车日期
     */
    private String firstPurchaseDate = null;

    /**
     * 评估单id
     */
    private String vehicleEvaluateInfoId = null;


    public String getBillTypeCode() {
        return "A001";
    }

}
