package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.service.IProductBizService;
import com.fuze.bcp.api.drools.bean.AccrualSubsidiesBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class CompensatoryPolicyController {

    @Autowired
    private IProductBizService iProductBizService;


    /**
     * 【PAD-API】 获取贴息政策
     *
     * @param accrual
     * @return
     */
    @RequestMapping(value = "/compensatory", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getCompensatory(@RequestBody AccrualSubsidiesBean accrual) {
/*        accrual.setMonths(18);
        accrual.setRatio(0.06);*/
        return iProductBizService.actGetCompensatory(accrual);
    }


    /**
     * 【PAD-API】分页获取贴息政策
     *
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/compensatorys", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCompensatoryPage(@RequestParam("date") String date, @RequestParam("pageindex") Integer pageIndex, @RequestParam("pagesize") Integer pageSize) {
        return iProductBizService.actGetCompensatoryPolicyPage(date, pageIndex, pageSize);
    }
}
