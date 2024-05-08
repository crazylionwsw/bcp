package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.paymentBill.PaymentBillSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IPaymentBillBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ${Liu} on 2018/3/5.
 */
@RestController
@RequestMapping(value = "/json")
public class PaymentBillController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(PaymentBillController.class);

    @Autowired
    IPaymentBillBizService iPaymentBillBizService;

    /**
     *创建缴费单
     */
    @RequestMapping(value = "/paymentbill/{transactionId}/init",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean postPaymentBill(@PathVariable("transactionId") String transactionId){
        return iPaymentBillBizService.actCreatePaymentBill(transactionId);
    }

    /**
     *提交缴费单
     */
    @RequestMapping(value = "/paymentbill",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean aubmitPaymentBill(@RequestBody PaymentBillSubmissionBean paymentBillSubmissionBean){
        return iPaymentBillBizService.actSubmitPaymentBill(paymentBillSubmissionBean);
    }

    /**
     *缴费单列表
     */
    @RequestMapping(value = "/paymentbills",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getPaymentBills(@RequestParam(value = "pageindex", defaultValue = "0", required = false) Integer pageIndex, @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pageSize, @RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed){
        return iPaymentBillBizService.actGetPaymentBills(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

    /**
     * 根据Id获取缴费单
     */
    @RequestMapping(value = "/paymentbill/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getPaymentBill(@PathVariable("id") String paymentBillId){
        return iPaymentBillBizService.actGetPaymentBillInfo(paymentBillId);
    }

    /**
     * 获取缴费单提交次数
     */
    @RequestMapping(value = "/paymentbills/{id}/count",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getPaymentBillCount(@PathVariable("id") String transactionId){
        return iPaymentBillBizService.actGetPaymentBillCount(transactionId);
    }

}
