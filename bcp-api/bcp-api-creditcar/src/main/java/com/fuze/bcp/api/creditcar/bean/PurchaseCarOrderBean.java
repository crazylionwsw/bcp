package com.fuze.bcp.api.creditcar.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.bd.bean.FeeValueBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户购车签约信息
 */
@Data
public class PurchaseCarOrderBean extends APICarBillBean {

    /**
     * 购车信息
     */
    private String customerCarId = null;

    /**
     * 借款需求
     */
    private String customerLoanId = null;

    /**
     * 4s店销售
     */
    private String dealerEmployeeId = null;

    /**
     * 家庭总车辆数
     */
    private Integer carNumber;

    /**
     * 贷款购车次数
     */
    private Integer buyCarNumber;

    /**
     * 是否需要打印收入证明
     * 1 true
     * 0 false
     */
    private Integer printNeedEarningProof = 0;

    /**
     *  反担保人
     */
    private String counterGuarantorId = null;

    /**
     * 是否要求代启卡
     * 1 true
     * 0 false
     */
    private Integer replaceStartCard = 1;

    /**
     * 预约提车日期
     */
    private String pickCarDate = null;

    /**
     * 收费明细列表，用编码标识不同的费用项目
     * 贷款服务费，担保服务费，风险押金 杂费
     */
    List<FeeValueBean> feeItemList = new ArrayList<FeeValueBean>();

    /**
     * 每月还款金额
     */
    private String repaymentAmountMonthly;

    /**
     * 首月还款金额
     */
    private String repaymentAmountFirstMonth;

    /**
     * 总还款金额
     */
    private String repaymentAmountSum;

    /**
     * 评估单id
     */
    private String vehicleEvaluateInfoId = null;

    /**
     * 是否为直客模式
     * 1：是  0：否
     */
    private int isStraight = 0;

    /**
     * 指标状态
     *  1   :   现标
     *  2   :   外迁
     *  3   :   报废
     *  4   :   出售
     */
    private Integer indicatorStatus = 1;

    /**
     * 外迁日期
     */
    private String moveOutDate = null;

    /**
     * 预计指标获取日期
     */
    private String retrieveDate = null;

    /**
     * 情况说明
     */
    private String indicatorComment = null;

    /**
     * 定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A002";
    }

}
