package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.CancelOrderBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;

/**
 * 订单取消申请的服务接口
 */
public interface ICancelOrderBizService {

    /**
     * 保存订单取消申请单 （暂存，不进审批流）
     * @param cancelOrderBean
     * @return
     */
    ResultBean<CancelOrderBean> actSaveCancelOrder(CancelOrderBean cancelOrderBean);

    /**
     * 提交订单取消申请单 （保存，并进审批流）
     * @return
     */
    ResultBean<CancelOrderBean> actSubmitCancelOrder(String tid, String comment);

    /**
     * 获取订单取消申请单
     * @param id
     * @return
     */
    ResultBean<CancelOrderBean> actGetCancelOrder(String id);

    /**
     *      通过交易ID查询取消业务数据
     * @param transactionId
     * @return
     */
    ResultBean<CancelOrderBean> actGetCancelOrderByTransactionId(String transactionId);

    /**
     * 审核
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<CancelOrderBean> actSignCancelOrder(String id, SignInfo signInfo);

    /**
     * 取消业务模糊查询（升级后）
     * @param searchBean
     * @return
     */
    ResultBean<CancelOrderBean> actSearchCancelOrders(String userId, SearchBean searchBean);

    /**
     * 获取某分期经理的订单取消申请单
     * @param loginUserId 分期经理的用户ID
     * @param currentPage
     * @return
     */
    ResultBean<List<CancelOrderBean>> actGetCancelOrders(String loginUserId, Integer currentPage);

}
