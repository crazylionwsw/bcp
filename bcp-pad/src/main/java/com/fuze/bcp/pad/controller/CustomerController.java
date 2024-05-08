package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class CustomerController {

    @Autowired
    ICustomerBizService iCustomerBizService;

//    @Autowired
//    ICustomerTransactionBizService iCustomerTransactionBizService;


//    /**
//     * 【PAD - API】--我的在办业务列表
//     *
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "/user/{loginuserid}/transactions", method = RequestMethod.GET)
//    @ResponseBody
//    public ResultBean getMyTransactions(@PathVariable("loginuserid") String loginUserId, @RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pageSize) {
//        System.out.println(loginUserId);
//        List<Integer> tmpList = new ArrayList<>();
//        tmpList.add(CustomerTransactionBean.TRANSACTION_INIT);
//        tmpList.add(CustomerTransactionBean.TRANSACTION_PROCESSING);
//        tmpList.add(CustomerTransactionBean.TRANSACTION_ORDER);
//        tmpList.add(CustomerTransactionBean.TRANSACTION_LOAN);
//        return this.iCustomerTransactionBizService.actGetTransactionsByLoginUserId(loginUserId, tmpList, pageIndex, pageSize);
//    }

    /**
     * 【PAD API】-- 获取单个客户接口
     *
     * @return
     * @params
     */
    @RequestMapping(value = "/customer/{customerid}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOneTransaction(@PathVariable("customerid") String customerId) {
        return iCustomerBizService.actGetCustomerById(customerId);
    }

//    /**
//     * 【PAD API】-- 我的全部业务
//     *
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "/user/{loginuserid}/transactions/all", method = RequestMethod.GET)
//    @ResponseBody
//    public ResultBean getMyAllTransactions(@PathVariable("loginuserid") String loginUserId, @RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pageSize) {
//        DataPageBean<PadCustomerTransactionBean> page = iCustomerTransactionBizService.actGetAllTransactionsByLoginUserId(loginUserId, pageIndex, pageSize).getD();
//        return ResultBean.getSucceed().setD(page);
//    }

}
