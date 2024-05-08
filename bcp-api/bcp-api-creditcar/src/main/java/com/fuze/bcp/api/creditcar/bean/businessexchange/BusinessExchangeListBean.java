package com.fuze.bcp.api.creditcar.bean.businessexchange;

import com.fuze.bcp.api.creditcar.bean.APIBillListBean;
import com.fuze.bcp.bean.RateType;
import lombok.Data;

/**
 * Created by ${Liu} on 2018/3/1.
 */
@Data
public class BusinessExchangeListBean extends APIBillListBean{

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 身份证号
     */
    private String identifyNo;

    /**
     * 手机号
     */
    private String cell;

    /**
     * 分期申请车价
     */
    private Double applyAmount;

    /**
     * 首付金额
     */
    private Double downPayment;

    /**
     * 首付比例
     */
    private Double downPaymentRatio;

    /**
     * 贷款额度
     */
    private Double creditAmount = 0.0;

    /**
     * 贷款的比例
     */
    private Double creditRatio;


    /**
     * 经销商名称
     */
    private String cardealerName;

    /**
     * 经销商地址
     */
    private String carDealerAddress = null;

    /**
     * 车型
     */
    private String fullName;

    /**
     * 颜色
     */
    private String carColor = null;

    /**
     * 车排量
     */
    private String carEmissions = null;

    /**
     * 审核状态
     */
    private Integer approveStatus;

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

    /**
     * 业务类型 (新车/二手车）
     */
    private String businessTypeCode;

    /**
     * 交易Id
     */
    private String customerTransactionId;




}
