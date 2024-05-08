package com.fuze.bcp.api.customer.service;

import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 客户服务接口类
 * Created by sean on 2017/5/19.
 */
public interface ICustomerBizService {

    /**
     * 根据客户ID查询客户信息
     *
     * @param id
     * @return
     */
    ResultBean<CustomerBean> actGetCustomerById(@NotNull String id);

    /**
     * 保存客户信息
     *
     * @param customerBean
     * @return
     */
    ResultBean<CustomerBean> actSaveCustomer(CustomerBean customerBean);

    /**
     *      保存客户年收入
     * @param customerBean
     * @return
     */
    ResultBean<CustomerBean> actSaveCustomerSalary(CustomerBean customerBean);

    /**
     * 提交客户信息
     * @param customerBean
     * @return
     */
    ResultBean<CustomerBean> actSubmitCustomer(CustomerBean customerBean);



    /**
     * 获取客户信息(带分页)
     *
     * @param currentPage
     * @return
     */
    ResultBean<CustomerBean> actGetCustomers(@NotNull @Min(0) Integer currentPage);

    //获取可用客户
    ResultBean<List<CustomerBean>> actGetAvaliableCustomer();

    //获取客户信息(带分页,时间倒序)
    ResultBean<CustomerBean> actGetCustomersOrderByTs(@NotNull @Min(0) Integer currentPage);

    /**
     * 删除客户信息
     *
     * @param id
     * @return
     */
    ResultBean<CustomerBean> actDeleteCustomer(@NotNull String id);

    /**
     * 模糊查询客户，不带分页
     *
     * @param customerBean
     * @return
     */
    ResultBean<List<CustomerBean>> actSearchCustomer(CustomerBean customerBean);

    ResultBean<DataPageBean<CustomerBean>> searchCustomer(Integer currentPage, CustomerBean customerBean);

    ResultBean<List<CustomerBean>> actGetCustomerQuery(String loginUserId, String inputStr);

    /**
     * 通过身份证号获取客户
     *
     * @param identifyNo
     * @return
     */
    ResultBean<CustomerBean> actGetCustomerByIdentifyNo(String identifyNo);

    /**
     * 保存车
     * @param customerCarBean
     * @return
     */
    ResultBean<CustomerCarBean> actSaveCustomerCar(CustomerCarBean customerCarBean);

    /**
     *   根据 transactionId 获取 CustomerCar
     * @param transactionId
     * @return
     */
    ResultBean<CustomerCarBean> actGetCustomerCarByTransactionId(String transactionId);

    ResultBean<List<CustomerCarBean>> actGetCustomerCarsByTransactionId(String transactionId);

    /**
     * 保存贷款
     * @param customerLoanBean
     * @return
     */
    ResultBean<CustomerLoanBean> actSaveCustomerLoan(CustomerLoanBean customerLoanBean);

    /**
     *  根据 transactionId 获取 CustomerLoan
     * @param transactionId
     * @return
     */
    ResultBean<CustomerLoanBean> actGetCustomerLoanByTransactionId(String transactionId);

    ResultBean<List<CustomerLoanBean>> actGetCustomerLoansByTransactionId(String transactionId);

    /**
     * 根据ID查询车辆资质信息中的购车信息
     * @param id
     * @return
     */
    ResultBean<CustomerCarBean> actGetCustomerCarById(String id);

    /**
     * 根据ID查询车辆资质信息中的借款信息
     * @param id
     * @return
     */
    ResultBean<CustomerLoanBean> actGetCustomerLoanById(String id);

    /**
     *根据交易ID查询借款信息
     */
    ResultBean<CustomerLoanBean> actGetCustomerLoan(String customerTransactionId);

    /**
     * 根据Id获取客户卡信息
     * @param id
     * @return
     */
    ResultBean<CustomerCardBean> actGetCustomerCardById(String id);

    /**
     * 根据交易Id获取客户卡信息
     * @param customerTransactionId
     * @return
     */
    ResultBean<CustomerCardBean> actGetCustomerCardByCustomerTransactionId(String customerTransactionId);

    /**
     * 保存客户信息
     * @param customerCardBean
     * @return
     */
    ResultBean<CustomerCardBean> actSaveCustomerCard(CustomerCardBean customerCardBean);

    /**
     *      计算 贴息方案
     * @param bankFeeAmount
     * @param compensatoryAmount
     * @param months
     * @return
     */
    ResultBean<Map<String,Object>> actCalculateCompensatoryWay(Double bankFeeAmount, Double compensatoryAmount, Integer months);

    /**
     * 根据交易Id获取客户还款信息
     * @param customerTransactionId
     * @return
     */
    ResultBean<CustomerRepaymentBean> actGetCustomerRepaymentByCustomerTransactionId(String customerTransactionId);

    /**
     *  更新客户的单位信息
     * @param id
     * @param customerJobBean
     * @return
     */
    ResultBean<CustomerBean> actUpdateCustomerJob(String id, CustomerJobBean customerJobBean);

    /**
     * 保存客户还款信息
     * @param customerRepaymentBean
     * @return
     */
    ResultBean<CustomerRepaymentBean> actSaveCustomerRepayment(CustomerRepaymentBean customerRepaymentBean);

    /**
     *根据客户Id查询客户车信息
     */
    ResultBean<CustomerCarBean> actGetCustomerCar(String customerId);


    ResultBean<List<CustomerLoanBean>>  actFindAllCustomerLoan();

    ResultBean<CustomerBean> actSaveCustomerType(String id, Integer isSelfEmployed);

    //保存业务调整车辆信息
    ResultBean<CustomerCarExchangeBean> actSaveCustomerExchangeCar(CustomerCarExchangeBean customerCarExchangeBean);

    //保存业务调整借款信息
    ResultBean<CustomerLoanExchangeBean> actSaveCustomerExchangeLoan(CustomerLoanExchangeBean customerLoanExchangeBean);

    //获取业务调账单借款信息
    ResultBean<CustomerLoanExchangeBean> actGetCustomerExchangeLoan(String exchangeLoanId);

    //获取业务调账单购车信息
    ResultBean<CustomerCarExchangeBean> actGetCustomerExchangeCar(String exchangeCarId);

    //通过交易id查询业务调整的借款exchangeLoan信息
    ResultBean<CustomerLoanExchangeBean> actGetCustomerExchangeLoanByCustomerTransactionId(String customerTransactionId);

    /**
     *通过交易id查询业务调整的车信息exchangecar信息
     */
    ResultBean<CustomerCarExchangeBean> actGetCustomerExchangeCarByCustomerTransactionId(String customerTransactionId);



}
