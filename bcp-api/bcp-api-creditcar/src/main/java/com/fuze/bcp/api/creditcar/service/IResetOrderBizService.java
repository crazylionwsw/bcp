package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.ResetOrderBean;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * 重新签约的服务接口
 */
public interface IResetOrderBizService {

    /**
     * 保存重新签约（暂存，不进审批流）
     * @param resetOrderBean
     * @return
     */
    ResultBean<ResetOrderBean> actSaveResetOrder(ResetOrderBean resetOrderBean);

    /**
     * 提交重新签约 （保存，并进审批流）
     * @param resetOrderBean
     * @return
     */
    ResultBean<ResetOrderBean> actSubmitResetOrder(ResetOrderBean resetOrderBean);

    /**
     * 获取重新签约
     * @param id
     * @return
     */
    ResultBean<ResetOrderBean> actGetResetOrder(String id);

    /**
     * 通过交易ID查询重新签约数据
     * @param transactionId
     * @return
     */
    ResultBean<ResetOrderBean> actGetResetOrderByTransactionId(String transactionId);

    /**
     * 审核
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<ResetOrderBean> actSignResetOrder(String id, SignInfo signInfo);

    /**
     * 重新签约查询列表
     * @param currentPage
     * @param approveStatus
     * @return
     */
    ResultBean<ResetOrderBean> actGetResetOrders(int currentPage, int approveStatus);

    /**
     *  模糊查询 重新签约
     * @param currentPage
     * @param approveStatus
     * @param customerBean
     * @return
     */
    ResultBean<ResetOrderBean> actSearchResetOrders(int currentPage, int approveStatus, CustomerBean customerBean);

    /**
     * 获取某分期经理的订单取消申请单
     * @param loginUserId 分期经理的用户ID
     * @param currentPage
     * @return
     */
    ResultBean<List<ResetOrderBean>> actGetResetOrders(String loginUserId, Integer currentPage);

}
