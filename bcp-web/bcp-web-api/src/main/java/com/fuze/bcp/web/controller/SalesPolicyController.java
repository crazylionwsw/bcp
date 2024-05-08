package com.fuze.bcp.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.bd.bean.SalesPolicyBean;
import com.fuze.bcp.api.bd.service.IProductBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class SalesPolicyController {

    @Autowired
    private IProductBizService iProductBizService;

    /**
     * 获取销售政策数据
     */
    @RequestMapping(value = "/salespolicys", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSalesPolicys() {
        return iProductBizService.actSalesPolicys();
    }

    /**
     * 保存销售政策数据
     *
     * @param salesPolicy
     * @return
     */
    @RequestMapping(value = "/salespolicy", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveSalesPolicy(@RequestBody SalesPolicyBean salesPolicy) {
        return iProductBizService.actSaveSalesPolicy(salesPolicy);
    }

    /**
     * 删除销售政策数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/salespolicy/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteSalesPolicy(@PathVariable("id") String id) {
        return iProductBizService.actDeleteSalesPolicy(id);
    }

    /**
     * 获取销售政策数据
     *
     * @return
     */
    @RequestMapping(value = "/salespolicy/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupSalesPolicys() {
        return iProductBizService.actLookupSalesPolicies();
    }
}
