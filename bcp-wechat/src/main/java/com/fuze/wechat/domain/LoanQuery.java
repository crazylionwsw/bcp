package com.fuze.wechat.domain;

import com.fuze.wechat.base.DataStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by ${Liu} on 2018/4/18.
 * 信用贷借款额度查询
 */
@Document(collection = "so_loan_query")
@Data
public class LoanQuery implements Serializable, DataStatus{

    private static final long serialVersionUID = -1L;

    /**
     * 主键信息
     */
    @Id
    private String id;

    /**
     * 数据状态：暂存，保存，作废， 锁定状态
     */
    private Integer dataStatus = DataStatus.SAVE;

    /**
     * 客户姓氏
     */
    private String customerName;

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
     *  社保月缴存额
     */
    private Integer socialInsuranceAmount;

    /**
     *  公积金月缴存额
     */
    private Integer accumulationFundAmount;

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
     * 缴费方式
     * 月交  MONTHLYPAY
     * 年交  YEARPAY
     * 趸交  WHOLE
     */
    private String payType;

    /*****************房贷月供***************/

    /**
     * 房产位置
     *
     * 北京人   BEIJING
     * 其他地区  OTHER
     */
    private String houseAddress;

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
     * 借款额度
     *  注：  由后台程序计算获取
     */
    private Double loanAmount;

    private String ts;

    /**
     * 客户针对微信公众号的opinId
     */
    private String openId;

    /**
     * 分期经理针对微信公众号的openId
     */
    private String shareOpenId;

    /**
     * 客户针对微信小程序的opinId
     */
    private String mpOpenId;

    /**
     * 分期经理针对微信小程序的openId
     */
    private String shareMpOpenId;

    /**
     *  数据创建类型
     *      WP      微信公众号
     *      MP      微信小程序
     */
    private String createType;

}
