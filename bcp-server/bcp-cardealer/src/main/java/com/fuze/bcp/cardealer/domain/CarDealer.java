package com.fuze.bcp.cardealer.domain;

import com.fuze.bcp.api.bd.bean.PaymentPolicyBean;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.PayAccount;
import com.fuze.bcp.bean.SalesRate;
import com.fuze.bcp.bean.ServiceFee;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import static com.fuze.bcp.api.cardealer.bean.CarDealerBean.*;

/**
 * 4S 店经销商信息
 */
@Document(collection = "bd_cardealer")
@Data
public class CarDealer extends BaseDataEntity implements ApproveStatus {

    /**
     * 所属集团
     */
    private String dealerGroupId;

    /**
     * 审核状态
     */
    private Integer approveStatus = APPROVE_INIT;

    /**
     * 经销商来源
     */
    private Integer sourceDeals = SOURCE_FIRST;

    /**
     * 经营业务 (编码)
     */
    private List<String> businessTypeCodes = new ArrayList<String>();

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
     * 系统用户
     */
    private String loginUserId = null;
    /**
     * 分期经理
     */
    private List<String> businessManIds = null;

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
     * 经销商贴息占比
     */
    private String compensatoryRatio = null;

    /**
     * 贷款总额
     */
    private String sumLoan = null;

    /**
     * 销售地区
     */
    private List<String> provinceIds = new ArrayList<String>();

    /**
     * 部门领导
     */
    private String leaderId;

    /**
     * 报单行
     */
    private String cashSourceId = null;

    /**
     * 渠道合作支行---表示该渠道与某某支行合作
     */
    private String cooperationCashSourceId = null;

    /**
     * 是否限定品牌
     *  1：   限定      0：未限定
     */
    private Integer brandIsLimit = 0;

    /**
     *  支付方式
     *  0   :   差额支付
     *  1   :   全额支付
     */
    private Integer paymentMethod;

    /**
     * 新车垫资时间点(1 代表开票前,2 代表开票后)
     */
    private String paymentNewTime;

    /**
     * 二手车垫资时间点(1 代表初交易,2 代表登记后)
     */
    private String paymentOldTime;

    /**
     * 备注信息
     */
    private String commentInfo;

    /**
     * 终端销售手续费率
     * String  业务类型编码
     * Map<业务类型编码,Map<资金利率编码,List<RateType>>>
     */
    private List<SalesRate> dealerRateTypes = new ArrayList<SalesRate>();

    /**
     * 贷款服务费率
     */
    private List<ServiceFee> serviceFeeEntityList = new ArrayList<ServiceFee>();

    /**
     *  垫资策略
     */
    private PaymentPolicyBean paymentPolicy = null;

    /**
     * 保存选中的系统销售政策的ID
     */
    private String salesPolicyId;

    /**
     * 单据编码
     * @return
     */
    public String getBillTypeCode() {
        return "A022";
    }
}