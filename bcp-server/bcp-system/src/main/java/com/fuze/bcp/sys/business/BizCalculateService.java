package com.fuze.bcp.sys.business;

import com.fuze.bcp.api.sys.service.ICalculateBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.sys.service.ICalculateService;
import com.fuze.bcp.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class BizCalculateService implements ICalculateBizService {

    @Autowired
    ICalculateService iCalculateService;

    @Autowired
    MessageService messageService;

    /**
     *
     * 计算贷款金额
     *
     * @param monthNum
     * @param creditAmount
     * @param businessTypeCode
     * @return
     */
    @Override
    public ResultBean<Map<String, Object>> actGetCalculateMoney(Integer monthNum, Double creditAmount, String businessTypeCode) {
        if (monthNum == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CALCULATE_MONTH_NULL"));
        }
        if (creditAmount == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CALCULATE_CREDITAMOUNT_NULL"));
        }
        if (StringHelper.isBlock(businessTypeCode)) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CALCULATE_BUSINESSTYPECODE_NULL"));
        }
        return iCalculateService.calculateMoney(monthNum, creditAmount, businessTypeCode);
    }

    /**
     * 获取贷款结果列表
     *
     * @param paymentWay       支付方式
     * @param creditAmount     贷款金额
     * @param businessTypeCode 业务类型
     * @return
     */
    @Override
    public ResultBean<List<Map<String, Object>>> actGetCalculateMoneys(String paymentWay, Double creditAmount, String businessTypeCode) {
        if (paymentWay == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CALCULATE_PAYMENTWAY_NULL"));
        }
        if (creditAmount == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CALCULATE_CREDITAMOUNT_NULL"));
        }
        if (StringHelper.isBlock(businessTypeCode)) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CALCULATE_BUSINESSTYPECODE_NULL"));
        }
        return iCalculateService.calculateMoneys(paymentWay, creditAmount, businessTypeCode);
    }


    /**
     * PC端
     *
     * @param monthNum         贷款月份
     * @param creditAmount     贷款金额
     * @param charge           银行手续费
     * @param businessTypeCode 业务类型 OC NC
     * @param chargePaymentWay 资金来源
     * @return
     */
    @Override
    public ResultBean<Map<String, Object>> actGetCalculateMoneyWithBusinessTypeCodeAndChargePaymentWay(Integer monthNum, Double creditAmount, Double charge, String businessTypeCode, String chargePaymentWay) {
        return iCalculateService.calculateMoneyWithBusinessTypeCodeAndChargePaymentWay(monthNum, creditAmount, charge, businessTypeCode, chargePaymentWay);
    }

}
