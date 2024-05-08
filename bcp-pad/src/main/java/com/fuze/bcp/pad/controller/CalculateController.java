package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.sys.service.ICalculateBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class CalculateController {

    @Autowired
    ICalculateBizService iCalculateBizService;

    /**
     *
     * 【PAD - API】--分期计算器接口
     *
     * @param paymentWay              贷款期数
     * @param creditAmount          贷款金额
     * @param businessTypeCode      OC_BANK_CHARGE_RATE  NC_BANK_CHARGE_RATE
     * @return
     */
    @RequestMapping(value = "/calculates", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCalculate(@RequestParam(value = "paymentway") String paymentWay, @RequestParam(value = "creditamount") double creditAmount, @RequestParam(value = "bizcode") String businessTypeCode) {
        return iCalculateBizService.actGetCalculateMoneys(paymentWay, creditAmount, businessTypeCode);
    }
}
