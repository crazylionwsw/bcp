package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.creditcar.bean.OrderListBean;
import com.fuze.bcp.api.creditcar.bean.OrderSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;
import java.util.Map;

/**
 * 客户签约接口
 * Created by Lily on 2017/7/19.
 */
public interface IOrderBizService {

    /**
     * 资质提交成功后初始化签约数据
     * @param customerDemandId
     * @return
     */
    ResultBean<OrderSubmissionBean> actCreateOrder(String customerDemandId) ;

    /**
     * 提交客户签约单 （正式保存，同时提交进审批流）
     *
     * @param id
     * @param comment
     * @return
     */
    ResultBean<PurchaseCarOrderBean> actSubmitOrder(String id, String comment);

    /**
     * 签约审核
     * @param orderId  签约单的ID
     * @param signInfo 签批意见
     * @return
     */
    ResultBean<PurchaseCarOrderBean> actSignOrder(String orderId, SignInfo signInfo);

    /**
     * 通过客户ID和交易ID获取签约
     *
     * @param customerId            客户ID
     * @param customerTransactionId 交易ID
     * @return
     */
    ResultBean<PurchaseCarOrderBean> actGetOrder(String customerId, String customerTransactionId);

    /**
     * 通过ID获取签约单
     *
     * @param orderId 签约单的ID
     * @return
     */
    ResultBean<PurchaseCarOrderBean> actGetOrder(String orderId);

    /**
     * 客户签约模糊查询（升级后）
     * @param searchBean
     * @return
     */
    ResultBean<PurchaseCarOrderBean> actSearchOrders(String userId, SearchBean searchBean);

    /**
     * 保存客户签约数据（WEB端）
     *
     * @param purchaseCarOrderBean
     * @return
     */
    ResultBean<PurchaseCarOrderBean> actSaveOrder(PurchaseCarOrderBean purchaseCarOrderBean);

    /**
     * 保存客户签约单（PAD端，保存，不进审批流）
     *
     * @param orderSubmission
     * @return
     */
    ResultBean<OrderSubmissionBean> actSaveOrder(OrderSubmissionBean orderSubmission);

    /**
     * 获取分期经理的签约单列表
     *
     * @param loginUserId （分期经理的用户ID）
     * @param currentPage
     * @return
     */
    ResultBean<List<OrderListBean>> actGetOrders(String loginUserId, Integer currentPage);

    /**
     * 根据  交易ID        查询客户签约数据
     *
     * @param transactionId 交易ID
     * @return
     */
    ResultBean<PurchaseCarOrderBean> actGetOrderByTransactionId(String transactionId);

    /**
     * 根据  交易ID         组装客户签约数据
     *
     * @param transactionId 交易ID
     * @return
     */
    ResultBean<OrderSubmissionBean> actInitOrderByTransactionId(String transactionId);

    /**
     * 根据交易ID获取签约数据
     * @param transactionId
     * @return
     */
    ResultBean<OrderSubmissionBean> actGetTransactionOrder(String transactionId) ;


    /**
     * 获取分期经理签约信息
     * @param isPass
     * @param loginUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean<DataPageBean<OrderListBean>> actGetOrders(Boolean isPass,String loginUserId, Integer pageIndex, Integer pageSize);

    /**
     * 获取日报数据信息
     */
    ResultBean<Map<Object,Object>> getDailyReport(String orgid,String date, PurchaseCarOrderBean t);

    ResultBean<Map<Object, Object>> getDailyReport(String orgId,String date);

    /**
     * 获取渠道数据信息
     */
    ResultBean<Map<Object,Object>> getChannelReport(String date, PurchaseCarOrderBean t,String loginUserId);

    /**
     * 获取渠道数据信息
     */
    ResultBean<Map<Object,Object>> getEmployeeReport(String date, PurchaseCarOrderBean t,String employeeId);
    /**
     * 获取统计数据
     * @param as
     * @return
     */
    ResultBean<List<Map>> getAllCustomerByApproveStatus(Integer as);

    ResultBean<List<CustomerLoanBean>> findAllCustomerLoan();

    ResultBean<Map> actCompareData(String propname, String transactionId);

    //  客户签约通过，给分期经理发送批贷函
    void actSendPurchaseCarOrderDocument(String id);

    /**
     * 获取客户签约的审核状态
     */
    ResultBean<String> actGetOrderApproveStatus(String transactionId);

    /**
     * 获取客户签约时的所有收费项
     */
    ResultBean<List<FeeValueBean>> actGetAllFeesOnOrder(String transactionId);


}