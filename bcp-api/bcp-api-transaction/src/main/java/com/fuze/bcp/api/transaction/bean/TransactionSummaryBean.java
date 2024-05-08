package com.fuze.bcp.api.transaction.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 客户交易
 */
@Data
public class TransactionSummaryBean extends APIBaseBean {

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 审核状态
     */
    private Integer approveStatus;

    /**
     * 交易状态
     */
    private Integer status = 0;

    /***********************************************客户信息***********************************************/
    /**
     * 客户姓名
     */
    private String customerName = null;

    /**
     * 性别： -1：未标识，0：男性，1：女性
     */
    private Integer customerGender = -1;

    /**
     * 客户电话
     */
    private String customerCell = null;

    /**
     * 客户身份证号
     */
    private String customerIdentifyNo = null;

    /***********************************************车辆信息************************************************/
    /**
     * 车型名称
     */
    private String carTypeName = null;
    /**
     * 车型ID
     */
    private String carTypeId = null;

    /**
     * 颜色
     */
    private String carColor = null;

    /**
     * 官方指导价
     */
    private Double guidePrice = 0.0;

    /**
     * 评估价
     */
    private Double evaluatePrice = 0.0;

    /**
     * 车排量
     */
    private String carEmissions = null;

    /***********************************************借款信息***********************************************/
    /**
     * 在资质审查页面 为 预计成交价
     * 在客户签约中   为 车辆实际成交价
     */
    private Double realPrice;

    /**
     * 开票价
     */
    private Double receiptPrice;

    /**
     * 贷款额度
     */
    private Double creditAmount;

    /**
     * 贷款的比例
     */
    private Double creditRatio;

    /**
     * 首付金额
     */
    private Double downPayment;

    /**
     * 首付比例
     */
    private Double downPaymentRatio;

    /**
     * 银行批复贷款额度
     */
    private Double approvedCreditAmount;

    /**
     * 贷款期数（月份）
     */
    private Integer months = 0;

    /**
     * 贷款利率
     */
    private Double ratio = 0.0;

    /**
     * 是否贴息
     */
    private Integer compensatoryInterest = 0;

    /***********************************************经销商信息***********************************************/
    /**
     * 经销商Id
     */
    private String carDealerId = null;
    /**
     * 经销商名称
     */
    private String carDealerName = null;

    /**
     * 经销商名称
     */
    private String carDealerAddress = null;

    /**
     * 业务类型 (新车/二手车）
     */
    private String businessTypeCode;

    /**
     * 业务阶段
     */
    private TransactionStageBean transactionStage;

}
