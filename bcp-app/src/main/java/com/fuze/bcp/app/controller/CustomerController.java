package com.fuze.bcp.app.controller;

import com.fuze.bcp.api.customer.bean.CustomerCardBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lily on 2017/12/27.
 */
@RestController
@RequestMapping(value = "/json/app")
public class CustomerController {

    @Autowired
    ICustomerBizService iCustomerBizService;

    /**
     * 保存客户卡信息
     * @param customerCardBean
     * @return
     */
    @RequestMapping(value = "/customercard", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCustomerCard(@RequestBody CustomerCardBean customerCardBean) {
        return iCustomerBizService.actSaveCustomerCard(customerCardBean);
    }
}
