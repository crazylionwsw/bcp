package com.fuze.bcp.api.cardealer.bean;

import com.fuze.bcp.api.bd.bean.PaymentPolicyBean;
import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.bean.ApproveStatus;
import lombok.Data;

import java.util.List;

import static com.fuze.bcp.api.cardealer.bean.CarDealerBean.STATUS_CONTRACT;

/**
 * 4S 店经销商信息
 */
@Data
public class CarDealerListBean extends APIBaseDataBean implements ApproveStatus{

    /**
     * 经营业务
     */
    private List<String> businessTypes;

    /**
     * 所在地址
     */
    private String address = null;

    /**
     * 手机
     */
    private String cell = null;

    /**
     * 经销商员工数
     */
    private Integer countOfEmployees = 0;

    /**
     * 分期经理
     */
    private List<String> businessMans = null;

    /**
     * 分期经理ID
     */
    private List<String> businessManIds = null;

    /**
     * 合作状态，默认是合同签署中
     */
    private Integer status = STATUS_CONTRACT;

    /**
     * 审核状态
     */
    private Integer approveStatus = APPROVE_INIT;

    /**
     * 是否限定品牌
     */
    private Integer brandIsLimit = 0;

    /**
     *  垫资策略
     */
    private PaymentPolicyBean paymentPolicy = null;

}