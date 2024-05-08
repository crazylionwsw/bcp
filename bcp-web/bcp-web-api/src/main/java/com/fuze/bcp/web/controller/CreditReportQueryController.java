package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.CreditReportQueryBean;
import com.fuze.bcp.api.creditcar.service.ICreditReportQueryBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerDemandBizService;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lily on 2017/7/22.
 */
@RestController
@RequestMapping("/json")
public class CreditReportQueryController {

    private static final Logger logger = LoggerFactory.getLogger(CreditReportQueryController.class);

    @Autowired
    private ICreditReportQueryBizService iCreditReportQueryBizService;

    @Autowired
    private ICustomerBizService iCustomerBizService;

    @Autowired
    private ICustomerDemandBizService iCustomerDemandBizService;

    @RequestMapping(value = "/creditreportquerys", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CreditReportQueryBean> getCreditReportQueries(
            @RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
            @RequestParam(value = "approveStatus", required = false, defaultValue = "1") int approveStatus) {
        return iCreditReportQueryBizService.actFindCreditReportQueries(currentPage, approveStatus);
    }

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/creditreportquery/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CreditReportQueryBean> getCreditReportQueryById(@PathVariable("id") String id) {
        return iCreditReportQueryBizService.actFindCreditReportQueryById(id);
    }
}
