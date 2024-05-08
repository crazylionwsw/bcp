package com.fuze.bcp.sys.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.sys.domain.SysParam;
import com.fuze.bcp.sys.service.ICalculateService;
import com.fuze.bcp.sys.service.ISysParamService;
import com.fuze.bcp.utils.CalculatorUtil;
import com.fuze.bcp.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.Format;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by GQR on 2017/9/12.
 */
@Service
public class CalculateServiceImpl implements ICalculateService {

    @Autowired
    private ISysParamService iSysParamService;

    //PAD端
    @Override
    public ResultBean<Map<String, Object>> calculateMoney(Integer monthNum, Double creditAmount, String businessTypeCode) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Double ratioNum = null;
        //根据业务类型编码，得到利率的 map
        ResultBean<Map<?, ?>> resultBean = iSysParamService.getMap(businessTypeCode);
        if (resultBean.isSucceed()) {
            String ratioStr = (String) resultBean.getD().get(monthNum.toString());
            if (ratioStr != null || ratioStr != "") {
                ratioNum = Double.parseDouble(ratioStr);
            }
        }

        Double monthlyPayments = CalculatorUtil.calculatorMonthlyPayments(creditAmount, monthNum);
        Double firstPayment = CalculatorUtil.calculatorFirstPayment(creditAmount, monthNum);
        resultMap.put("ratio", resultBean.getD() == null ? 0 : ratioNum * 100);
        resultMap.put("charge", resultBean.getD() == null ? 0 : FormatUtils.formatInteger(ratioNum * creditAmount));
        resultMap.put("monthlyPayments", FormatUtils.formatInteger(monthlyPayments));
        resultMap.put("firstPayment", FormatUtils.formatInteger(firstPayment));

        return resultBean.getSucceed().setD(resultMap).setM("success");
    }

    //PAD端
    @Override
    public ResultBean<List<Map<String, Object>>> calculateMoneys(String paymentWay, Double creditAmount, String businessTypeCode) {
        List<Map<String, Object>> list = new ArrayList<>();
        //根据业务类型编码，得到利率的 map
        SysParam sysParam = iSysParamService.getOneByCode(businessTypeCode);
        if (sysParam == null) {
            return ResultBean.getFailed().setM("系统参数错误，分期计算失败！");
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map params = objectMapper.readValue(sysParam.getParameterValue(), Map.class);
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Map resultMap = new HashMap();
                Integer month = Integer.parseInt((String) entry.getKey());
                Double ratio = Double.parseDouble((String) entry.getValue());
                Double charge = FormatUtils.formatDigit(ratio * creditAmount, 0);
                Double monthlyPayments = CalculatorUtil.calculatorMonthlyPayments(creditAmount, charge, month, paymentWay);
                Double firstPayment = CalculatorUtil.calculatorFirstPayment(creditAmount,charge, month,paymentWay);
                resultMap.put("ratio", ratio * 100);
                resultMap.put("month",month);
                resultMap.put("charge", charge);
                resultMap.put("monthlyPayments", FormatUtils.formatInteger(monthlyPayments));
                resultMap.put("firstPayment", FormatUtils.formatInteger(firstPayment));
                list.add(resultMap);
            }
        } catch (IOException i) {
            return ResultBean.getFailed().setM("系统错误！");
        }
        if (list == null || list.isEmpty()) {
            return ResultBean.getFailed().setM("计算失败，请重试！");
        } else {
            return ResultBean.getSucceed().setD(list);
        }
    }

    //PC端
    @Override
    public ResultBean<Map<String, Object>> calculateMoneyWithBusinessTypeCodeAndChargePaymentWay(Integer monthNum, Double creditAmount, Double charge, String businessTypeCode, String chargePaymentWay) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Double monthlyPayments = CalculatorUtil.calculatorMonthlyPayments(creditAmount, charge, monthNum, chargePaymentWay);
        Double firstPayment = CalculatorUtil.calculatorFirstPayment(creditAmount, charge, monthNum, chargePaymentWay);
        resultMap.put("monthlyPayments", FormatUtils.formatInteger(monthlyPayments));
        resultMap.put("firstPayment", FormatUtils.formatInteger(firstPayment));
        return ResultBean.getSucceed().setD(resultMap).setM("success");
    }
}
