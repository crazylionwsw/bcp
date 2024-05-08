package com.fuze.bcp.api.creditcar.bean.bankcardapply;

import com.fuze.bcp.bean.ImageTypeFileBean;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡业务app详情信息
 * Created by Lily on 2017/12/26.
 */
@Data
public class BankCardBean extends APIBaseBean {

    /**
     * 客户交易ID
     */
    private String customerTransactionId;

    /**
     * 提交人的用户ID （提交人可能是业务员或后台操作人员）
     */
    private String loginUserId = null;

    /**
     * 提交人的员工ID
     */
    private String employeeId = null;

    /**
     * 业务类型
     */
    private String businessTypeCode = null;

    /**
     *客户姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String identifyNo;

    /**
     * 卡号
     */
    private String cardNo = null;

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

    /**
     * 刷卡金额
     */
    private Double swipingMoney = null;

    /**
     * 刷卡期数
     */
    private Integer swipingPeriods = null;

    /**
     * 银行申请制卡时间
     */
    private String applyTime = null;

    /**
     * 分行取卡时间
     */
    private String takeTime = null;

    /**
     * 代启卡时间
     */
    private String replaceActivateTime = null;

    /**
     * 代启卡人
     */
    private String replaceActivateName = null;

    /**
     * 调额时间
     */
    private String changeAmountTime = null;

    /**
     * 领卡时间
     */
    private String receiveTrusteeTime = null;

    /**
     * 领卡人
     */
    private String receiveCardName = null;

    /**
     * 代刷卡时间
     */
    private String swipingTrusteeTime = null;

    /**
     * 刷卡人
     */
    private String swipingName = null;

    /**
     * 销卡时间
     */
    private String cancelCardTime = null;

    /**
     * 首次还款日(YYYY-MM-DD)
     */
    private String firstReimbursement = null;

    /**
     * 卡业务状态
     */
    private Integer status ;

    /**
     * 首期还款额
     */
    private Double firstRepayment;

    /**
     * 月还款额
     */
    private Double monthRepayment;

    /**
     * 总还款额
     */
    private Double totalRepayment = null;

    //档案资料
    private List<ImageTypeFileBean> customerImages = new ArrayList<ImageTypeFileBean>();
}
