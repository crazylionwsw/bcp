package com.fuze.bcp.api.sys.service;

import com.fuze.bcp.bean.ResultBean;

import java.util.List;
import java.util.Map;

/**
 * PAD端APP管理
 */
public interface ICalculateBizService {

    //PAD端

    /**
     * 获取贷款结果
     *
     * @param monthNum         贷款期限
     * @param creditAmount     贷款金额
     * @param businessTypeCode 业务类型
     * @return
     */
    ResultBean<Map<String, Object>> actGetCalculateMoney(Integer monthNum, Double creditAmount, String businessTypeCode);

    /**
     * 获取贷款结果列表
     *
     * @param paymentWay       支付方式
     * @param creditAmount     贷款金额
     * @param businessTypeCode 业务类型
     * @return
     */
    ResultBean<List<Map<String, Object>>> actGetCalculateMoneys(String paymentWay, Double creditAmount, String businessTypeCode);

    //PC端
    ResultBean<Map<String, Object>> actGetCalculateMoneyWithBusinessTypeCodeAndChargePaymentWay(Integer monthNum, Double creditAmount, Double charge, String businessTypeCode, String chargePaymentWay);

}
