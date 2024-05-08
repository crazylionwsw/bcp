package com.fuze.bcp.api.wechat.bean;

import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

/**
 * Created by ${Liu} on 2018/4/18.
 */
@Data
public class LoanQueryBean extends APIBaseDataBean {

    /**
     * 客户姓氏
     */
    private String customerName;

    /**
     * 客户微信opinId
     */
    private String openId;

    /**
     * 电话
     */
    private String cell;

    /**
     * 客户性别
     */
    private Integer gender;

    /********打卡工资**********/

    /**
     * 打卡工资金额
     */
    private Integer workSalary;

    /**
     * 有无社保
     */
    private Integer isSocialInsurance;

    /**
     * 有无公积金
     */
    private Integer isAccumulationFund;

    /**
     * 单位性质
     * 公务员/事业单位 Institutions / civil servants  "ICS"
     * 上市公司         Listed company      "LC"
     * 民营企业         Private enterprise  "PE"
     */
    private String companyType;

    /**
     * 工作年限
     * 半年以上
     * 一年以上
     * 五年以上
     */
    private String workDate;

    /*********商业保险**********/

    /**
     * 是否投保人
     */
    private Integer isPolicyHolder;

    /**
     * 商业保险年缴额
     * Annual payment of commercial insurance
     * 2400以上
     * 5000以上
     * 10000以上
     */
    private Integer apoci;

    /**
     * 已缴费时间
     * 1年以上     ONEYEARUP
     * 2年以上     TWOYEARUP
     * 3年以上     THREEYEARUP
     */
    private String payDate;

    /**
     * 中间有无断缴
     */
    private Integer isInterrupt;

    /**
     * 保险类型
     * 寿险   Life insurance
     * 分红险  participating insurance
     * 万能险  Universal Insurance
     */
    private String insuranceType;

    /**
     * 缴费方式
     * 月交  MONTHLYPAY
     * 年交  YEARPAY
     * 趸交  WHOLE
     */
    private String payType;

    /*****************房贷月供***************/

    /**
     * 月供类型
     * 抵押贷  pledge loan
     * 经营贷  Business loan
     */
    private String monthlyPaymentType;

    /**
     * 是否本地月供
     *
     *北京人北京月供   BPBM
     * 外地人北京月供  WPBM
     * 外地人外地月供  WPWM
     */
    private String isLocalMonthly;

    /**
     * 月供金额
     * 1500
     * 3000
     * 5000
     */
    private Integer monthlyAmount;

    /**
     * 已还时间
     * 1个月以上    MONTHLYUP
     * 1年以上     ONEYEARUP
     * 2年以上     TWOYEARUP
     * 3年以上     THREEYEARUP
     */
    private String repayDate;

    /**
     *  预计借款额度
     */
    private Double expectedLoanAmount;

    /**
     * 预计借款期限
     *  1   --   4   年
     */
    private Integer expectedLoanTime;

    /**
     * 分享人Id (分期经理)
     */
    private String shareOpenId;

    /**
     * 借款额度
     *  注：  由后台程序计算获取
     */
    private Double loanAmount;

}
