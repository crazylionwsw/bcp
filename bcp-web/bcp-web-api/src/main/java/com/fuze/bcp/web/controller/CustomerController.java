package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.service.ICreditReportQueryBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCardBean;
import com.fuze.bcp.api.customer.bean.CustomerJobBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class CustomerController {

    @Autowired
    private ICustomerBizService iCustomerBizService;

    @Autowired
    private ICreditReportQueryBizService iCreditReportQueryBizService;

    /**
     * 获取客户列表数据(带分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomers(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iCustomerBizService.actGetCustomersOrderByTs(currentPage);
    }

    /**
     * 保存客户信息
     *
     * @param customer
     * @return
     */
    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCustomer(@RequestBody CustomerBean customer) {
        return iCustomerBizService.actSaveCustomer(customer);
    }

    @RequestMapping(value = "/customer/salary", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCustomerSalary(@RequestBody CustomerBean customer) {
        return iCustomerBizService.actSaveCustomerSalary(customer);
    }

    /**
     * 查询单条客户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerById(@PathVariable("id") String id) {
        return iCustomerBizService.actGetCustomerById(id);
    }

    /**
     * 通过客户ID获取征信查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/customer/{id}/creditreportquery", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerCreditReportQuery(@PathVariable("id") String id) {
        return iCustomerBizService.actGetCustomerById(id);
    }

    /**
     * 删除客户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCustomer(@PathVariable("id") String id) {
        return iCreditReportQueryBizService.actFindByCustomerId(id);
    }

    /**
     * 查询客户购买的车辆信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/customer/car/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerCarById(@PathVariable("id") String id) {
        return iCustomerBizService.actGetCustomerCarById(id);
    }

    /**
     * 根据客户Id查询购买车辆信息
     * @param customerId
     * @return
     */
    @RequestMapping(value = "/customer/{id}/car",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerCarByCustomerId(@PathVariable("id") String customerId){
        return iCustomerBizService.actGetCustomerCar(customerId);
    }

    /**
     * 查询客户的贷款信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/customer/loan/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerLoanById(@PathVariable("id") String id) {
        return iCustomerBizService.actGetCustomerLoanById(id);
    }

    /**
     * 保存客户借款信息
     * TODO:        暂时用于WEB端 保存银行批付额度功能，等待后期，给分期经理发送短信
     * @param customerLoanBean
     * @return
     */
    @RequestMapping(value = "/customer/loan", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCustomerLoan(@RequestBody CustomerLoanBean customerLoanBean) {
        return iCustomerBizService.actSaveCustomerLoan(customerLoanBean);
    }

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

    /**
     * 通过客户交易Id查询客户卡信息
     * @param customerTransactionId
     * @return
     */
    @RequestMapping(value = "/customercard", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerCardByCustomerTransactionId(@RequestParam(value = "customerTransactionId", defaultValue = "") String customerTransactionId) {
        return iCustomerBizService.actGetCustomerCardByCustomerTransactionId(customerTransactionId);
    }

    /**
     * 根据交易ID查询借款信息
     */
    @RequestMapping(value = "/customer/transaction/loan/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerLoanByCustomerTransactionId(@PathVariable("id") String customerTransactionId){
        return iCustomerBizService.actGetCustomerLoan(customerTransactionId);
    }

    /**
     *根据交易Id查询客户拟购车辆信息
     */
    @RequestMapping(value = "/customer/transaction/car/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerCarByCustomerTransactionId(@PathVariable("id") String customerTransactionId){
        return iCustomerBizService.actGetCustomerCarByTransactionId(customerTransactionId);
    }

    @RequestMapping(value = "/customer/job/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean updateCustomerJob(@PathVariable("id") String id, @RequestBody CustomerJobBean customerJobBean){
        return iCustomerBizService.actUpdateCustomerJob(id,customerJobBean);
    }

    @RequestMapping(value = "/customers/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean actSearchCustomer(@RequestParam(value = "currentPage", required = false, defaultValue = "0")
                                                    Integer currentPage,@RequestBody CustomerBean customerBean){
        return iCustomerBizService.searchCustomer(currentPage,customerBean);
    }

    @RequestMapping(value = "/customers/avaliable",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actSearchCustomer(){
        return iCustomerBizService.actGetAvaliableCustomer();
    }


    @RequestMapping(value = "/customer/{id}/{isSelfEmployed}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actUpdateCustomerType(@PathVariable("id") String id, @PathVariable("isSelfEmployed") Integer isSelfEmployed){
        return iCustomerBizService.actSaveCustomerType(id,isSelfEmployed);
    }

    /**
     * 获取客户业务调整借款数据
     */
    @RequestMapping(value = "/customer/adjust/loan/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerAdjustLoan(@PathVariable("id") String id){
        return iCustomerBizService.actGetCustomerExchangeLoan(id);
    }

    /**
     *获取客户业务调整购车数据
     */
    @RequestMapping(value = "/customer/adjust/car/{id}",method = RequestMethod.GET)
    @ResponseBody
    public  ResultBean getCustomerAdjustCar(@PathVariable("id") String id){
        return iCustomerBizService.actGetCustomerExchangeCar(id);
    }

    /**
     * 根据交易ID查询业务调整借款信息
     */
    @RequestMapping(value = "/customer/transaction/exchangeloan/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerExchangeLoanByCustomerTransactionId(@PathVariable("id") String customerTransactionId){
        return iCustomerBizService.actGetCustomerExchangeLoanByCustomerTransactionId(customerTransactionId);
    }

    /**
     * 根据交易ID查询业务调整车信息
     */
    @RequestMapping(value = "/customer/transaction/exchangecar/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerExchangeCarByCustomerTransactionId(@PathVariable("id") String customerTransactionId){
        return iCustomerBizService.actGetCustomerExchangeCarByCustomerTransactionId(customerTransactionId);
    }
}
