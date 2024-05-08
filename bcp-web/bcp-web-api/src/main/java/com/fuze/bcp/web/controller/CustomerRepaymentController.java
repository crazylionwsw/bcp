package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

/** 客户还款信息
 * Created by ${Liu} on 2017/9/23.
 */
@RestController
@RequestMapping(value = "/json")
public class CustomerRepaymentController {

    @Autowired
    ICustomerBizService iCustomerBizService;

    /**
     * 通过交易Id查询还款信息
     * @param customerTransactionId
     * @return
     */
    @RequestMapping(value = "/customer/repayment/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerRepayment(@PathVariable("id") String customerTransactionId){
        return iCustomerBizService.actGetCustomerRepaymentByCustomerTransactionId(customerTransactionId);
    }


}
