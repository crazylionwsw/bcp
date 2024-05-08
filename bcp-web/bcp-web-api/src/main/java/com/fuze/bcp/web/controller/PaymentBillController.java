package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.paymentBill.PaymentBillBean;
import com.fuze.bcp.api.creditcar.service.IPaymentBillBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ${Liu} on 2018/3/7.
 */
@RestController
@RequestMapping(value = "/json")
public class PaymentBillController {

    @Autowired
    IPaymentBillBizService iPaymentBillBizService;

    /**
     *获取缴费单列表(含查询)
     */
    @RequestMapping(value = "/paymentbills/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<PaymentBillBean> searchPaymentBill(@RequestBody SearchBean searchBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iPaymentBillBizService.actSearchPaymentBill(userId,searchBean);
    }

    /**
     *获取缴费单详情
     */
    @RequestMapping(value = "/paymentbill/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PaymentBillBean> getPaymentBill(@PathVariable("id") String paymentBillId){
        return iPaymentBillBizService.actGetPaymentBill(paymentBillId);
    }

    /**
     *缴费单审核
     */
    @RequestMapping(value = "/paymentbill/{id}/sign",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean signPaymentBill(@PathVariable("id") String id, @RequestBody SignInfo signInfo){
        return iPaymentBillBizService.actSignPaymentBill(id,signInfo);
    }

    /**
     * 根据交易id查询几条缴费单
     * @param id
     * @return
     */
    @RequestMapping(value = "/paymentbill/count/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<Integer> getOneByTransactionId(@PathVariable("id") String id) {
        return iPaymentBillBizService.actGetPaymentBillCount(id);
    }

    /**
     * 根据交易id查询多条缴费单
     * @param id
     * @return
     */
    @RequestMapping(value = "paymentbills/transaction/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PaymentBillBean> getPaymentsByTransactionId(@PathVariable("id") String id){
        return iPaymentBillBizService.actGetPaymentBillsByTransactionId(id);
    }

}
