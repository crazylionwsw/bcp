package com.fuze.bcp.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zqw on 2017/11/24.
 */
@Data
public class SearchBean implements Serializable {

    public static int DEFAULT_PAGESIZE = 20;

    //  客户交易阶段
    public static int STAGE_TRANSACTION = 0;
    //  资质审查阶段
    public static int STAGE_DEMAND = 1;
    //  客户签约及以后所有的阶段
    public static int STAGE_ORDER = 2;

    //  客户信息
    private String name;
    private String identifyNo;
    private String pledgeName;
    private String mateName;


    //  部门信息：部门ID，分期经理ID,渠道ID
    private String orginfoId;
    private String businessManagerId;
    private String carDealerId;
    private String cashSourceId;

    //  贷款信息
    private Double startLimitAmount;
    private Double endLimitAmount;
    private Integer creditMonths = 0;
    private Boolean compensatory = true;//贴息
    private Boolean business = true;//商贷

    //  业务类型
    private Boolean nc = true;//新车
    private Boolean oc = true;//二手车

    //  日期信息
    private String timeName = "ts";
    private String startTime;
    private String endTime;
    private String dayTime;
    //  审批时间
    private String startApproveTime;
    private String endApproveTime;

    //  分页属性
    private Integer currentPage;
    private Integer pageSize = DEFAULT_PAGESIZE;

    //  状态信息
    //  example : status、approveStatus
    private String statusName;
    private Integer statusValue;

    //  垫资支付，支付方式：开票前支付（1）、开票后支付（2）、全部（0）
    private Integer advancedPay = 0;

    //  单据编码
    private String billTypeCode;

    //排序名称
    private String sortName = "ts" ;

    //排序方式
    private String sortWay = "DESC";

    //刷卡时间
    private String swipingCardTime;

}