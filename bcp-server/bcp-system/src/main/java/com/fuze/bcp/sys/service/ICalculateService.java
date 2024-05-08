package com.fuze.bcp.sys.service;

import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.IBaseService;
import com.fuze.bcp.sys.domain.LoginLog;

import java.util.List;
import java.util.Map;

/**
 * Created by GQR on 2017/9/12.
 */

public interface ICalculateService {

    //PAD端
    public ResultBean<Map<String, Object>> calculateMoney(Integer monthNum, Double creditAmount, String businessTypeCode);

    public ResultBean<List<Map<String, Object>>> calculateMoneys(String paymentWay, Double creditAmount, String businessTypeCode);

    //PC端
    public ResultBean<Map<String, Object>> calculateMoneyWithBusinessTypeCodeAndChargePaymentWay(Integer monthNum, Double creditAmount, Double charge, String businessTypeCode, String chargePaymentWay);


}
