package com.fuze.bcp.api.statistics.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import lombok.Data;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GQR on 2017/10/23.
 */
@Data
@MongoEntity(entityName = "so_chargefeeplan")
public class ChargeFeePlanBean extends APIBaseBean implements ApproveStatus {


    /**
     * 业务初始状态
     */
    public final static int STATUS_INIT = 0;

    /**
     * 代码支行ID
     */
    private String cashSourceId = null;

    /**
     * 业务数据正常状态
     */
    public final static int STATUS_NORMAL = 1;

    /**
     * 数据核对通过状态
     */
    public final static int STATUS_CHEKCED_PASS = 2;

    /**
     * 数据核对错误状态
     */
    public final static int STATUS_CHEKCED_ERROR = 3;

    /**
     * 数据核对错误状态
     */
    public final static int STATUS_CHEKCED_CANCEL = 4;

    /**
     * 业务错误状态
     */
    public final static int STATUS_ERROR = 9;




    private Integer status = STATUS_INIT;

    /**
     * 客户ID
     */
    private String customerId = null;

    /**
     * 核对人ID
     */
    private String checkUserId = null;

    /**
     * 渠道ID
     */
    private String cardealerId = null;

    /**
     * 客户交易ID
     */
    private String customerTransactionId = null;

    /**
     * 贷款金额
     */
    private Double limitAmount = null;

    /**a
     * 刷卡日期
     */
    private String swingCardDate = null;

    /**
     * 签约时间
     */
    private String orderTime = null;

    /**
     * 首次还款日
     */
    private String firstRepaymentDate = null;

    /**
     * 员工ID
     */
    private String employeeId = null;

    /**
     * 截止还款日
     */
    private String endRepaymentDate = null;


    /**
     * 手续费缴纳方式
     */
    private String chargePaymentWay = null;

    /**
     * 借款期限
     */
    private Integer creditMonths = 0;

    /**
     * 手续费率
     */
    private Double bankChargeRatio = 0.0;

    //还款日
    private  Integer defaultReimbursement= null;

    /**
     * 手续费
     */
    private Double chargeAmount = 0.0;


    /**
     * 根据业务刷卡当月发生的数据计算返佣比率，以后每个月的结费沿用此结费政策
     */
    private Double chargeCheckRatio = 0.0;

    /**
     * 还款总额
     */
    private Double totalAmount = 0.0;

    /**
     * 审核信息
     */
    private List<SignInfo> signInfos = new ArrayList<SignInfo>();

    /**
     * 收费计划明细
     */

    private List<ChargeFeePlanDetailBean> detailList = new ArrayList<ChargeFeePlanDetailBean>();


    public void addSignInfo(SignInfo signInfo){
        this.signInfos.add(signInfo);
    }

    public void clearDetail(){
        if(this.detailList != null){
            detailList.clear();
        }
    }
    public void addOneDetail(ChargeFeePlanDetailBean rpd){
        if(detailList == null){
            detailList = new ArrayList<ChargeFeePlanDetailBean>();
        }
        detailList.add(rpd);
    }

    public ChargeFeePlanDetailBean createOneDetail(ChargeFeePlanBean chargeFeePlan){
        return new ChargeFeePlanDetailBean(chargeFeePlan.getId());
    }

    public void addOneDetail(ChargeFeePlanBean chargeFeePlan,int index,String date,Double amount,Double charge){
        ChargeFeePlanDetailBean rpd = createOneDetail(chargeFeePlan);
        rpd.setChargeFeePlanId(chargeFeePlan.getId());
        rpd.setIndex(index);
        rpd.setChargeAmount(charge);
        rpd.setYear(date.substring(0,4));
        rpd.setMonth(date.substring(5,7));
        detailList.add(rpd);

    }

    public ChargeFeePlanBean() {
        super();
        setDataStatus(DataStatus.SAVE);
    }

}
