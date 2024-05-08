package com.fuze.bcp.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.CreditPhotographBean;
import com.fuze.bcp.api.creditcar.service.ICreditPhotographBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 2017/6/21.
 */
@RestController
@RequestMapping(value = "/json")
public class CreditPhotographController {

    private static final Logger logger = LoggerFactory.getLogger(CreditPhotographController.class);

    @Autowired
    ICreditPhotographBizService iCreditPhotographBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    /**
     * 获取征信拍照列表
     *
     * @return
     */
    @RequestMapping(value = "/creditphotographs", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CreditPhotographBean> getCreditPhotographs(
            @RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
            @RequestParam(value = "uploadFinish", required = false, defaultValue = "") Boolean uploadFinish) {
        return iCreditPhotographBizService.actFindCreditPhotographs(currentPage, uploadFinish);
    }

    /**
     * 查询用户信息
     *
     * @param currentPage
     * @param uploadFinish
     * @param customer
     * @return
     */
    @RequestMapping(value = "/creditphotographs/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CreditPhotographBean> searchCreditReportQueries(@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                                      @RequestParam(value = "uploadFinish", required = false, defaultValue = "") Boolean uploadFinish,
                                                                      @RequestBody CustomerBean customer) {
        //查询Customer,
        List<CustomerBean> customers = iCustomerBizService.actSearchCustomer(customer).getD();
        List<String> customerIds = new ArrayList<String>();
        for (CustomerBean c : customers) {
            customerIds.add(c.getId());
        }
        return iCreditPhotographBizService.actFindAllByuploadFinishAndCustomerIds(currentPage, uploadFinish, customerIds);
    }

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/creditphotograph/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCreditPhotographById(@PathVariable("id") String id) {
        return iCreditPhotographBizService.actFindCreditPhotographById(id);
    }

    /**
     * 生成pdf并保存
     */
    @RequestMapping(value = "/creditphotograph/pdf", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getCreditPhotographById(@RequestBody CreditPhotographBean creditPhotographBean) {
        return iCreditPhotographBizService.actCreateCreditReportManually(creditPhotographBean);
    }

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/creditphotograph/bycustomerid/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCreditPhotographByCustomerId(@PathVariable("id") String customerId) {
        return iCreditPhotographBizService.actFindCreditPhotographByCustomerId(customerId);
    }


}

