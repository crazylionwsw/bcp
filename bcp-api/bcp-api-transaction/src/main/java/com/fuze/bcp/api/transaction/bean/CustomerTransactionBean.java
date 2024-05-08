package com.fuze.bcp.api.transaction.bean;

import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户交易
  */
@Data
public class CustomerTransactionBean extends APIBaseBean {

    /**
     * 初始化状态
     */
    public final static Integer TRANSACTION_INIT = 0;

    /**
     * 正在进行中
     */
    public final static Integer TRANSACTION_PROCESSING = 9;

    /**
     * 已经下订单状态  作废
     */
    @Deprecated
    public final static Integer TRANSACTION_ORDER = 1;

    /**
     * 处于正常贷款状态 作废
     */
    @Deprecated
    public final static Integer TRANSACTION_LOAN = 2;

    /**
     * 交易已完成
     */
    public final static Integer TRANSACTION_FINISH = 8;

    /**
     * 交易已取消
     */
    public final static Integer TRANSACTION_CANCELLED = 11;

    /**
     * 交易取消中
     */
    public final static Integer TRANSACTION_CANCELLING = 16;

    /**
     * 交易被拒绝
     */
    public final static Integer TRANSACTION_STOP = 18;

    /**
     * 已经转移到别的业务员   作废
     */
    @Deprecated
    public final static Integer TRANSACTION_TRANSFERED = 4;

    /**
     * 交易警报
     */
    public final static Integer TRANSACTION_WARNING = 20;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 目前所有的车
     */
    private List<CustomerCarBean> customerCarBean = new ArrayList<CustomerCarBean>();

    // 会话来源
    /**
     * 业务员
     */
    private String employeeId;

    /**
     * 用户ID
     */
    private String loginUserId;

    /**
     * 业务部门
     */
    private String orginfoId;

    /**
     * 来源经销商
     */
    private String carDealerId;

    /**
     *  渠道ID
     */
    private String channelId;

    /**
     * 报单行
     */
    private String cashSourceId;

    /**
     * 业务类型 (新车/二手车）
     */
    private String businessTypeCode;

    /**
     * 交易状态
     */
    private Integer status = TRANSACTION_INIT;

    /**
     * 所在阶段（每个阶段都有对应的单据，此处使用单据类型编码）
     */
    private String billTypeCode;

    /*
    * 客户的档案编号
    * */
    private String fileNumber = null;

    /**
     * 是否为直客模式
     * 1：是  0：否
     */
    private int isStraight = 0;
}
