package com.fuze.bcp.statistics.domain;

import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Changed By wangshaowei 2017-12-28.
 */
@Document(collection = "so_chargefeeplan")
@Data
public class ChargeFeePlan extends MongoBaseEntity implements ApproveStatus {

    /**
     * 业务初始状态
     */
    public final static int STATUS_INIT = 0;


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

    //public final static int STATUS_CHEKCED = 4;

    /**
     * 业务错误状态
     */
    public final static int STATUS_ERROR = 9;


    private Integer status = STATUS_INIT;

    private String cardealerId;



    /**
     * 核对人ID
     */
    private String checkUserId = null;


    /**
     * 还款日 默认为25号
     */
    private Integer  defaultReimbursement = null;
    /**
     * 贷款金额
     */
    private Double limitAmount = null;

    /**
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
    @Transient
    private List<ChargeFeePlanDetail> detailList = new ArrayList<ChargeFeePlanDetail>();


    public void addSignInfo(SignInfo signInfo){
        this.signInfos.add(signInfo);
    }

    public void clearDetail(){
        if(this.detailList != null){
            detailList.clear();
        }
    }
    public void addOneDetail(ChargeFeePlanDetail rpd){
        if(detailList == null){
            detailList = new ArrayList<ChargeFeePlanDetail>();
        }
        detailList.add(rpd);
    }

    public ChargeFeePlanDetail createOneDetail(ChargeFeePlan chargeFeePlan){
        return new ChargeFeePlanDetail(chargeFeePlan.getId());
    }

    public void addOneDetail(ChargeFeePlan chargeFeePlan,int index,String date,Double amount,Double charge){
        ChargeFeePlanDetail rpd = createOneDetail(chargeFeePlan);
        rpd.setChargeFeePlanId(chargeFeePlan.getId());
        rpd.setIndex(index);
        rpd.setChargeAmount(charge);
        rpd.setYear(date.substring(0,4));
        rpd.setMonth(date.substring(5,7));
        detailList.add(rpd);

    }

    public ChargeFeePlan() {
        super();
        setDataStatus(DataStatus.SAVE);
    }

    /**
     * 单据开始时间
     */
    private String startDate;
    /**
     * 审核状态
     */
    private Integer approveStatus = APPROVE_INIT;

    /**
     * 最后处理日期
     */
    private String approveDate = null;

    /**
     * 处理用户ID
     */
    private String approveUserId = null;

    /**
     * 业务类型
     */
    private String businessTypeCode = null;

    /**
     * 客户ID
     */
    private String customerId = null;

    /**
     * 客户交易ID
     */
    private String customerTransactionId = null;

    /**
     * 提交人的用户ID （提交人可能是业务员或后台操作人员）
     */
    private String loginUserId = null;

    /**
     * 提交人的员工ID
     */
    private String employeeId = null;

    /**
     * 业务来源，即4S店
     */
    private String  carDealerId = null;

    /**
     * 提交人所在的部门
     */
    private String orginfoId = null;

    /**
     *  报单行ID
     */
    private String cashSourceId = null;

    /**
     *  是否取消业务
     */
    private Integer cancelled = 0;

    public String getBillTypeCode() {
        return "A028";
    }
}
