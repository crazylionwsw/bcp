package com.fuze.bcp.api.transaction.bean;

import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.RateType;
import lombok.Data;

/**
 * 我的客户列表展示对象
 */
@Data
public class PadCustomerTransactionBean extends APIBaseBean {

    /***************************************************审核状态***************************************/
    /**
     * 初始化状态，未进入审核流程
     */
    public final static Integer APPROVE_INIT = 0;

    /**
     * 审核中，已经进入审核流程
     */
    public final static Integer APPROVE_ONGOING = 1;

    /**
     * 审核通过
     */
    public final static Integer APPROVE_PASSED = 2;

    /**
     * 审核驳回
     */
    public final static Integer APPROVE_REAPPLY = 8;

    /**
     * 审核被拒绝
     */
    public final static Integer APPROVE_REJECT = 9;

    /***************************************************任务状态********************************************/
    /**
     * 初始化状态
     */
    public final static Integer TRANSACTION_INIT = 0;

    /**
     * 正在进行
     */
    public final static Integer TRANSACTION_PROCESSING = 9;

    /**
     * 已经下订单状态
     */
    public final static Integer TRANSACTION_ORDER = 1;

    /**
     * 处于正常贷款状态
     */
    public final static Integer TRANSACTION_LOAN = 2;

    /**
     * 正常贷款完成状态
     */
    public final static Integer TRANSACTION_FINISH = 8;

    /**
     * 取消交易
     */
    public final static Integer TRANSACTION_CANCELLED = 11;

    /**
     * 取消中
     */
    public final static Integer TRANSACTION_CANCELLING = 16;

    /**
     * 已经转移到别的业务员
     */
    public final static Integer TRANSACTION_TRANSFERED = 4;

    /**
     * 客户ID
     */
    private String customerId = null;


    /**
     * 业务员
     */
    private String employeeId = null;

    /**
     * 用户ID
     */
    private String loginUserId = null;

    /**
     * 业务部门
     */
    private String orginfoId = null;

    /**
     * 来源经销商
     */
    private String carDealerId = null;

    /**
     * 业务类型 (新车/二手车）
     */
    private String businessTypeCode;

    /**
     * 交易状态
     */
    private Integer status = TRANSACTION_INIT;


    /***********************************************************************************************/

    /**
     * 客户姓名
     */
    private String customerMame = null;

    /**
     * 身份证号码
     */
    private String identifyNo;


    /**
     * 联系电话
     */
    private String cell;
    /***********************************************************************************************/


    /**
     * 经销商名称
     */
    private String carDealerName;

    private String carDealerAddress;

    /***********************************************************************************************/

    /**
     * 品牌名称 + 车系名称 + 车型名称
     */
    private String carString;

    /**
     * 车辆颜色
     */
    private String carColor;

    /**
     * 车辆颜色名称
     */
    private String carColorName;

    /**
     * 实际成交价
     */
    private Double realPrice;

    /**
     * 官方指导价（原价）
     */
    private Double guidePrice;

    /**
     * 评估价格
     */
    private Double evaluatePrice;

    /**
     * 车辆开票价格 = 贷款额度+首付金额
     */
    private Double receiptPrice;

    /***********************************************************************************************/

    /**
     * 手续费率
     */
    private RateType rateType = new RateType();

    /**
     * 贷款额度
     */
    private Double creditAmount = null;
    /**
     * 贷款的比例
     */
    private Double creditRatio = null;

    /**
     * 首付金额
     */
    private Double downPayment = null;

    /**
     * 首付比例
     */
    private Double downPaymentRatio = null;

    /**
     * 车辆颜色
     */
    private String customerDemandId;

    /***********************************************************************************************/

    /**
     * 审核状态
     */
    private Integer approveStatus = APPROVE_INIT;

    /**
     * 是否为直客模式
     * 1：是  0：否
     */
    private int isStraight = 0;


}
