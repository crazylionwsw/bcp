package com.fuze.bcp.api.customer.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 交易信息快照
 * 客户端展示客户的综合信息，包含人，车，钱信息
 * Created by sean on 16/10/10.
  */
@Data
public class CustomerInstantaneBean implements Serializable {

    //姓名
    //性别
    //身份证号
    //手机号码
    //车型全名
    //车型颜色
    //排量
    //业务类型
    //车价
    //贷款额度
    //首付金额
    //贷款期数
    //银行手续费率
    //经销商名称
    //经销商地址

    /**
     * 客户姓名
     */
    private String name = null;
    /**
     * 性别： -1：未标识，0：男性，1：女性
     */
    private Integer gender;
    /**
     * 身份证号码
     */
    private String identifyNo;

    /**
     * 联系电话
     */
    private String cell;

    /**
     * 车型全名
     */
    private String fullName;
    /**
     * 车型颜色
     */
    private String carColor;
    /**
     * 车排量
     */
    private String ml;
    /**
     * 业务类型 (新车/二手车）
     */
    private String businessTypeCode;
    /**
     * 评估价格　二手车
     */
    private String evaluatePrice;
    /**
     * 车辆成交价　新车
     */
    private String realPrice;
    /**
     * 贷款额度
     */
    private Double creditAmount;
    /**
     * 首付金额
     */
    private Double downPayment;
    /**
     * 期数（月份）
     */
    private Integer  months;
    /**
     * 期数（月份）
     */
    private String  dealerName;
    /**
     * 期数（月份）
     */
    private String  dealerAddress;
}
