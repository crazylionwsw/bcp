package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.bean.CompensatoryPolicyBean;
import com.fuze.bcp.api.bd.service.IProductBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/json")
public class CreditProductController {

    @Autowired
    private IProductBizService iProductBizService;


    /**
     * 【PAD-API】获取系统销售政策
     *
     * @return
     */
    @RequestMapping(value = "/salespolicys", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSalespolicys() {
        return iProductBizService.actGetPadSalesPolicys();
    }

    /**
     * 根据车牌id获取贴息政策
     * @param carBrandId
     * @return
     */
    @RequestMapping(value = "/salespolicy/{carBrandId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<CompensatoryPolicyBean>> getAppointPaymentsPage(@PathVariable("carBrandId") String carBrandId) {
        return iProductBizService.actGetCompensatoryPolicys(carBrandId);
    }

}
