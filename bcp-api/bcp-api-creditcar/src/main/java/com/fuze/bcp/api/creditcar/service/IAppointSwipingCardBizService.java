package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppAppointSwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardListBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardSubmissionBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;

/**
 * 预约刷卡的服务接口
 */
public interface IAppointSwipingCardBizService {

    /**
     * 客户签约提交后，通过监听来创建预约刷卡单
     *
     * @param orderId
     * @return
     */
    ResultBean<AppointSwipingCardBean> actCreateAppointSwipingCard(String orderId);

    /**
     * 保存预约刷卡单 （暂存）
     *
     * @param appointSwipingCardSubmissionBean
     * @return
     */
    ResultBean<AppointSwipingCardSubmissionBean> actSaveAppointSwipingCard(AppointSwipingCardSubmissionBean appointSwipingCardSubmissionBean);


    ResultBean<AppointSwipingCardBean> actSaveAppointSwipingCardInfo(AppointSwipingCardBean appointSwipingCardBean);

    /**
     * 提交预约刷卡单 （保存并进审批流）
     *
     * @param id
     * @return
     */
    ResultBean<AppointSwipingCardBean> actSubmitAppointSwipingCard(String id,String comment);

    /**
     * 获取分期经理的预约垫资单
     * @param isPass
     * @param loginUserId 分期经理的用户ID
     * @param currentPage
     * @return
     */
    ResultBean<List<AppointSwipingCardListBean>> actGetAppointSwipingCards(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize);

    /**
     * 根据交易ID获取详情数据
     *
     * @param transactionId
     * @return
     */
    ResultBean<AppointSwipingCardSubmissionBean> actInitAppointSwipingCardByTransactionId(String transactionId);

    /**
     * 获取预约刷卡单
     *
     * @param id
     * @return
     */
    ResultBean<AppointSwipingCardBean> actGetAppointSwipingCard(String id);

    /**
     * 根据交易ID获取预约刷卡单
     *      PAD
     * @param transactionId
     * @return
     */
    ResultBean<AppointSwipingCardSubmissionBean> actGetAppointSwipingCardByTransactionId(String transactionId);

    /**
     *  通过交易ID 获取预约刷卡数据
     *      WEB
     * @param transactionId
     * @return
     */
    ResultBean<AppointSwipingCardBean> actGetAppointSwipingCardByCustomerTransactionId(String transactionId);

    /**
     * 审核
     *
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<AppointSwipingCardBean> actSignAppointSwipingCard(String id, SignInfo signInfo);

    /**
     * 模糊查询(升级后)
     */
    ResultBean<AppointSwipingCardBean> actSearchAppointSwipingCards(String userId, SearchBean searchBean);

    /**
     *  更新业务状态
     * @param id
     * @param status
     * @return
     */
    ResultBean<AppointSwipingCardBean> actUpdateStatus(String id, Integer status);

    /**
     * app端获取预约刷卡信息
     * @param customerTransactionId
     * @return
     */
    ResultBean<AppAppointSwipingCardBean> actGetAppAppointSwipingCardByCustomerTransactionId(String customerTransactionId);

}
