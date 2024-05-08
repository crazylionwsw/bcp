package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.service.ICustomerFeeBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ${Liu} on 2017/9/27.
 */
@RestController
@RequestMapping(value = "/json")
public class CustomerFeeController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerFeeController.class);

    @Autowired
    private ICustomerFeeBizService iCustomerFeeBizService;

    /**
     *获取客户汇款信息
     */
    @RequestMapping(value = "/customer/fee/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerFeeBill(@PathVariable("id") String customerTransactionId){
        return iCustomerFeeBizService.actGetCustomerFeeBill(customerTransactionId);
    }
}
