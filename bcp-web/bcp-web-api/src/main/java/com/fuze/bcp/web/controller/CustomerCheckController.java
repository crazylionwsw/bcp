package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.customer.bean.CustomerCheckBean;
import com.fuze.bcp.api.customer.service.ICustomerCheckBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lily on 2017/8/3.
 */
@RestController
@RequestMapping("/json")
public class CustomerCheckController {

    @Autowired
    private ICustomerCheckBizService iCustomerCheckBizService;

    /**
     * 查询客户第三方核对信息
     * @param customerId
     * @return
     */
    @RequestMapping(value="/customercheck",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CustomerCheckBean> getCustomerCheckByCustomerId(@RequestParam String customerId){
        if(customerId != null){
            return iCustomerCheckBizService.actFindCustomerCheckByCustomerId(customerId);
        }
        return ResultBean.getFailed().setM("客户信息不存在!");
    }

    @RequestMapping(value="/customercheck/count",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerCheckCountByCustomerId(@RequestParam String customerId){
        return iCustomerCheckBizService.actFindCustomerCheckCountByCustomerId(customerId);
    }

}
