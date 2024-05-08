package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.annotation.MongoEntity;
import lombok.Data;

/**
 * 客户购物借款需求单
 * Created by Liy on 2017/07/19.
 */
@Data
@MongoEntity(entityName = "so_customerdemand")
public class CustomerDemandBean extends APICarBillBean {

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
    private String  pledgeCustomerId;

    /**
     * 配偶
     */
    private String mateCustomerId;

    /**
     * 贷款主体与客户之间关系
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
    private String  dealerEmployeeId;

    /**
     * 二手车首次购车日期
     */
    private String firstPurchaseDate = null;

    /**
     * 评估单id
     */
    private String vehicleEvaluateInfoId = null;

    /**
     * 定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A001";
    }

}
