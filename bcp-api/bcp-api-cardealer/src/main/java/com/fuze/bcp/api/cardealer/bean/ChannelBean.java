package com.fuze.bcp.api.cardealer.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.api.bd.bean.PaymentPolicyBean;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.SalesRate;
import com.fuze.bcp.bean.ServiceFee;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *     4s店
 */
@Data
@MongoEntity(entityName = "bd_channel")
public class ChannelBean extends ChannelBaseBean implements ApproveStatus {

    //  经营分类  【车分期、家装分期、交易分期、其他】【channelBase中的code】

    /**
     * 一级经销商
     */
    public final static Integer LEVEL_FIRST = 1;

    /**
     * 二级经销商
     */
    public final static Integer LEVEL_SECOND = 2;

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
     * 经销商贴息占比
     */
    private String compensatoryRatio = null;

    /**
     * 销售地区
     */
    private List<String> provinceIds = new ArrayList<String>();

    /**
     * 是否限定品牌
     */
    private Integer brandIsLimit = 0;

    /**
     *  支付方式
     *  0   :   差额支付
     *  1   :   全额支付
     */
    private Integer paymentMethod = 0;

    /**
     * 新车垫资时间点(1 代表发票前,2 代表发票后)
     */
    private Integer paymentNewTime;

    /**
     * 二手车垫资时间点(1 代表初交易,2 代表登记后)
     */
    private Integer paymentOldTime;

    /**
     * 终端销售手续费率
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
     * 审核状态
     */
    private Integer approveStatus = APPROVE_INIT;

    /**
     * 审批流的ID
     */
    private String activitiId = null;

    /**
     * 单据编码
     * @return
     */
    public String getBillTypeCode() {
        return "A022";
    }

}
