package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.CustomerDemandBean;
import com.fuze.bcp.api.creditcar.service.ICustomerDemandBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.TEMSignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lily on 2017/7/22.
 */
@RestController
@RequestMapping("/json")
public class CustomerDemandController {

    @Autowired
    private ICustomerDemandBizService iCustomerDemandBizService;

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/cardemand/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CustomerDemandBean> getOne(@PathVariable("id") String id) {
        return iCustomerDemandBizService.actFindCustomerDemandById(id);
    }

    /**
     *      审查、审批
     */
    @RequestMapping(value = "/cardemand/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CustomerDemandBean> sign(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iCustomerDemandBizService.actSignCustomerDemand(id, signInfo);
    }

    /**
     *  新审查、审批
     */
    @RequestMapping(value = "/cardemand/{id}/temsign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CustomerDemandBean> sign(@PathVariable("id") String id,@RequestBody TEMSignInfo temSignInfo) {
        return iCustomerDemandBizService.actSignCustomerDemand(id, temSignInfo);
    }

    @RequestMapping(value = "/cardemands/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CustomerDemandBean> searchCarDemands(@RequestBody SearchBean searchBean) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iCustomerDemandBizService.actSearchCustomerDemands(userId, searchBean);
    }

    /**
     * 保存 资质信息
     */
    @RequestMapping(value = "/cardemand", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CustomerDemandBean> save(@RequestBody CustomerDemandBean customerDemandBean) {
        return iCustomerDemandBizService.actSaveCustomerDemand(customerDemandBean);
    }

    /**
     *      根据     客户交易ID       查询资质审查数据
     * @param id    客户交易ID
     * @return
     */
    @RequestMapping(value = "/cardemand/transaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CustomerDemandBean> getOneByTransactionId(@PathVariable("id") String id) {
        return iCustomerDemandBizService.actGetByCustomerTransactionId(id);
    }

    /**
     * 保存 资质信息
     */
    @RequestMapping(value = "/cardemand/needGuarantor", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<CustomerDemandBean> saveCustomerDemandNeedCounterGuarantor(@RequestBody CustomerDemandBean customerDemandBean) {
        return iCustomerDemandBizService.actSaveCustomerDemandNeedCounterGuarantor(customerDemandBean);
    }

    /**
     * 根据客户id查询资质信息
     */
    @RequestMapping(value = "/customer/{id}/cardemand",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarDemandByCustomerId(@PathVariable("id") String id){
        return iCustomerDemandBizService.actFindCustomerDemandByCustomerId(id);
    }
}
