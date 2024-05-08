package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.bean.PadSalesRate;
import com.fuze.bcp.bean.PayAccount;
import com.fuze.bcp.bean.ServiceFee;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 4S 店经销商信息
 */
@Data
public class PadCarDealerBean extends APIBaseDataBean {

    /**
     * 开发阶段，合同在签署中
     */
    public final static Integer STATUS_CONTRACT = 0;

    /**
     * 正常合作状态
     */
    public final static Integer STATUS_ONGOING = 1;

    /**
     * 终止合作状态
     */
    public final static Integer STATUS_STOP = 9;

    /**
     * 一级经销商
     */
    public final static Integer LEVEL_FIRST = 1;

    /**
     * 二级经销商
     */
    public final static Integer LEVEL_SECOND = 2;

    /**
     * 自我开发
     */
    public final static Integer SOURCE_FIRST = 1;

    /**
     * 合作支行推荐
     */
    public final static Integer SOURCE_SECOND = 2;

    /**
     * 经销商来源
     */
    private Integer sourceDeals = SOURCE_FIRST;

    /**
     * 经营业务(业务类型编码）
     */
    private List<String> businessTypeCodes;

    /**
     * 所在地址
     */
    private String address = null;

    /**
     * 所在区域
     */
    private String dealerRegion = null;

    /**
     * 手机
     */
    private String cell = null;

    /**
     * 座机
     */
    private String telephone = null;

    /**
     * 审批流的ID
     */
    private String activitiId = null;

    /**
     * 负责人
     */
    private String manager = null;

    /**
     * 所属的公司部门
     */
    private String orginfoId = null;

    /**
     * 渠道经理
     */
    private String employeeId = null;

    /**
     * 分期经理
     */
    private List<String> businessManIds = null;

    /**
     * 分期经理
     */
    private List<String> businessManNames = null;

    /**
     * 系统用户
     */
    private String loginUserId = null;

    /**
     * 合作状态，默认是合同签署中
     */
    private Integer status = STATUS_CONTRACT;

    /**
     * 开始生效日期
     */
    private String startDate = null;

    /**
     * 经销商分为：一级和二级，一级是单品牌，二级是多品牌
     */
    private Integer level = LEVEL_FIRST;

    /**
     * 主营品牌
     */
    private List<String> carBrandIds;

    /**
     * 垫资支付周期为天 0---为当天支付   >0的正数为延期支付天数   <0的正数需要提前支付的天数
     */
    private Integer payPeriod = 0;

    /**
     * 财务账户信息
     */
    private List<PayAccount> payAccounts = null;

    /**
     * 经销商的月成交量
     */
    private String turnover = null;

    /**
     * 贷款客户数
     */
    private String customerNumber = null;

    /**
     * 贷款总额
     */
    private String sumLoan = null;

    /**
     * 经销商贴息占比
     */
    private String compensatoryRatio = null;

    /**
     * 销售地区
     */
    private List<String> provinceIds = new ArrayList<String>();

    /**
     * 部门领导
     */
    private String leaderId;

    /**
     * 合作支行
     */
    private String cashSourceId = null;

    /**
     * 渠道合作支行---表示该渠道与某某支行合作
     */
    private String cooperationCashSourceId = null;

    /**
     * 是否限定品牌
     */
    private Integer brandIsLimit = 0;

    /**
     * 支付方式(0 代表全额支付，1 代表差额支付)
     */
    private String paymentMethod;

    /**
     * 新车垫资时间点(0 代表发票前,1 代表发票后)
     */
    private String paymentNewTime;

    /**
     * 二手车垫资时间点(0 代表初交易,1 代表登记后)
     */
    private String paymentOldTime;

    /**
     * 备注信息
     */
    private String commentInfo;


    /**
     * 终端销售手续费率
     */
    private List<PadSalesRate> dealerRateTypes = new ArrayList<PadSalesRate>();
    /**
     * 贷款服务费率
     */
    private List<ServiceFee> serviceFeeEntityList = new ArrayList<ServiceFee>();
}