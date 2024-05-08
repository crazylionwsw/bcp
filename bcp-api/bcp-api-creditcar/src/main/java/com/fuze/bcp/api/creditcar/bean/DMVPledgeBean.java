package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 抵押单
 */
@Data
public class DMVPledgeBean extends APICarBillBean {

    /**
     * 登记证待收取
     */
    public final static int STATUS_EXPRESS = 0;
    /**
     * 登记证已收取
     */
    public final static int STATUS_RECEIVE = 1;
    /**
     * 银行抵押合同已打印
     */
    public final static int STATUS_CONTRACT = 2;
    /**
     * 银行抵押合同已盖章
     */
    public final static int STATUS_TAKED = 3;
    /**
     * 已送达车管所开始抵押
     */
    public final static int STATUS_START = 4;
    /**
     * 车管所抵押已完成
     */
    public final static int STATUS_END = 5;

    /**
     *  抵押状态
     */
    private Integer status = 0;

    /**
     * 抵押人Id
     */
    private String pledgeCustomerId = null;

    /**
     * 客户抵押资料签收时间
     */
    private String pledgeDateReceiveTime = null;

    /**
     * 银行抵押合同打印时间
     */
    private String contractStartTime =null;

    /**
     * 银行抵押合同盖章时间
     */
    private String takeContractTime =null;

    /**
     * 车管所抵押开始时间
     */
    private String pledgeStartTime = null;

    /**
     * 抵押完成时间
     */
    private String pledgeEndTime = null;

    /**
     * 存储完成的动作
     */
    private List<CardActionRecord> actionRecords = new ArrayList<CardActionRecord>();

    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A008";
    }
}
