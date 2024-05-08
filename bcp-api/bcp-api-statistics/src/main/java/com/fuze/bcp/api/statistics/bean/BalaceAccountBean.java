package com.fuze.bcp.api.statistics.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.utils.DateTimeUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GQR on 2017/11/7.
 */
@Data
@MongoEntity(entityName = "fi_balanceaccount")
public class BalaceAccountBean extends APIBaseBean {
    /**
     * 单据类型
     */
    private BillTypeBean billType;

    private final String billTypeCode = "A024";


    /**
     * 全部状态
     */
    public static final int CHECKSTATUS_ALL = -1;

    /**
     * 已计算待核对
     */
    public static final int CHECKSTATUS_INIT = 0;

    /**
     * 已核对待付款
     */
    public static final int CHECKSTATUS_CHECKED = 1;

    /**
     * 付款中待确认
     */
    public static final int CHECKSTATUS_PAY = 2;

    /**
     * 已付款，结算结束
     */
    public static final int CHECKSTATUS_FINISH =9;

    /**
     * 年份
     */
    private String year;

    /**
     * 月份
     */
    private String month;

    /**
     * 银行放款合计
     */
    private Double totalCredit = 0.0;

    /**
     * 客户数量
     */
    private Integer totalCount = 0;

    /**
     * 结算费用合计（包含本月趸交和分期的手续费）
     */
    private Double totalPaymentAmount = 0.0;

    /**
     * 结算状态
     */
    private Integer checkStatus = 0;



    /**
     * 结算人
     */
    private String loginUserId = null;

    /**
     *  核对人
     */
    private String checkUserId = null;
    /**
     * 审核信息
     */
    private List<SignInfo> signInfos = new ArrayList<SignInfo>();

    /**
     * 核对日期
     */
    private String checkDate = null;

    /**
     * 计算人
     */
    private String settleUserId = null;

    /**
     * 付款开始日期
     */
    private String paymentApplyDate = null;

    /**
     * 付款到账日期
     */
    private String paymentFinishDate = null;

    /**
     * 附件ID
     */
    private List<String> customerImageIds = new ArrayList<String>();

    public void updateTs(){
        setTs(DateTimeUtils.getCreateTime());
    }

    public void addSignInfo(SignInfo signInfo){
        this.signInfos.add(signInfo);
    }

}
