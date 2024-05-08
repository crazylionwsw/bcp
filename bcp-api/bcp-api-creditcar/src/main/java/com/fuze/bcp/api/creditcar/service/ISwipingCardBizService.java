package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardListBean;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardSubmissionBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;
import java.util.Map;

/**
 * 渠道刷卡
 * Created by Lily on 2017/9/15.
 */
public interface ISwipingCardBizService {
    /**
     * 创建渠道刷卡单
     * @param appointSwipingCardId
     * @return
     */
    ResultBean<SwipingCardBean> actCreateSwipingCard(String appointSwipingCardId);

    /**
     * 保存渠道刷卡单 （暂存）
     * @param swipingCardSubmissionBean
     * @return
     */
    ResultBean<SwipingCardSubmissionBean> actSaveSwipingCard(SwipingCardSubmissionBean swipingCardSubmissionBean);

    /**
     * 保存PDA提交的渠道刷卡单 （数据转换）
     * @param swipingCardSubmissionBean
     * @return
     */
    ResultBean<SwipingCardSubmissionBean> actSavePadSwipingCard(SwipingCardSubmissionBean swipingCardSubmissionBean);


    /**
     * 提交渠道刷卡单  （保存并进审批流）
     * @param id
     * @param comment
     * @return
     */
    ResultBean<SwipingCardBean> actSubmitSwipingCard(String id, String comment);

    /**
     * 获取分期经理的渠道刷卡单
     * @param isPass
     * @param loginUserId 分期经理的用户ID
     * @param currentPage
     * @return
     */
    ResultBean<List<SwipingCardListBean>> actGetSwipingCard(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize);

    /**
     * 根据交易ID获取详情数据
     * @param transactionId
     * @return
     */
    ResultBean<SwipingCardSubmissionBean> actInitSwipingCardByTransactionId(String transactionId);

    /**
     * 获取单个渠道刷卡
     */
    ResultBean<SwipingCardBean> actGetSwipingCard(String id);

    /**
     *  通过交易ID查询渠道刷卡信息
     * @param transactionId
     * @return
     */
    ResultBean<SwipingCardBean> actGetSwipingCardByTransactionId(String transactionId);

    /**
     *保存渠道刷卡信息
     */
    ResultBean<SwipingCardBean> actSaveSwipingcard(SwipingCardBean swipingCardBean);


    /**
     * 渠道刷卡模糊查询（升级后）
     * @param searchBean
     * @return
     */
    ResultBean<SwipingCardBean> actSearchSwipingCards(String userId, SearchBean searchBean);

    /**
     *  更新业务状态
     * @param id
     * @param status
     * @return
     */
    ResultBean<SwipingCardBean> actUpdateStatus(String id, Integer status);

    /**
     * 签批
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<SwipingCardBean> actSignSwipingCard(String id, SignInfo signInfo);

    /**
     * 获取渠道刷卡的日报数据
     */
    ResultBean<Map<Object,Object>> getDailyReport(String orgid,String date, SwipingCardBean t);

    /**
     *保存渠道刷卡数据(只保存)
     */
    ResultBean<SwipingCardBean> actSaveSwipingCardInfo(SwipingCardBean swipingCardBean);
}
