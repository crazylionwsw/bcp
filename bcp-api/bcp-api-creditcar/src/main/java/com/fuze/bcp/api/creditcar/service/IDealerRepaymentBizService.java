package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentBean;
import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentListBean;
import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentSubmissionBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;

/**
 * 渠道还款
 * Created by Lily on 2017/9/15.
 */
public interface IDealerRepaymentBizService {
    /**
     * 创建渠道还款单
     * @param orderId
     * @return
     */
    ResultBean<DealerRepaymentBean> actCreateDealerRepayment(String orderId);

    /**
     * 保存渠道还款单 （暂存）
     * @param swipingCardSubmissionBean
     * @return
     */
    ResultBean<DealerRepaymentSubmissionBean> actSaveDealerRepayment(DealerRepaymentSubmissionBean swipingCardSubmissionBean);


    /**
     * 提交渠道还款单  （保存并进审批流）
     * @param id
     * @param comment
     * @return
     */
    ResultBean<DealerRepaymentBean> actSubmitDealerRepayment(String id, String comment);

    /**
     * 获取分期经理的渠道还款单
     * @param isPass
     * @param loginUserId 分期经理的用户ID
     * @param currentPage
     * @return
     */
    ResultBean<List<DealerRepaymentListBean>> actGetDealerRepayments(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize);

    /**
     * 根据交易ID获取详情数据
     * @param transactionId
     * @return
     */
    ResultBean<DealerRepaymentSubmissionBean> actInitDealerRepaymentByTransactionId(String transactionId);

    /**
     *获取单个渠道还款信息
     */
    ResultBean<DealerRepaymentBean> actGetDealerRepayment(String id);

    /**
     * 保存渠道还款信息
     */
    ResultBean<DealerRepaymentBean> actSaveDealerRepayment(DealerRepaymentBean dealerRepaymentBean);

    /**
     * 渠道还款信息模糊查询
     * @param searchBean
     * @return
     */
    ResultBean<DealerRepaymentBean> actSearchDealerRepayment(String userId, SearchBean searchBean);

    /**
     * 更新业务状态
     * @param id
     * @param status
     * @return
     */
    ResultBean<DealerRepaymentBean> actUpdateStatus(String id, Integer status);

    /**
     * 签批
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<DealerRepaymentBean> actSignDealerRepayment(String id, SignInfo signInfo);
}
