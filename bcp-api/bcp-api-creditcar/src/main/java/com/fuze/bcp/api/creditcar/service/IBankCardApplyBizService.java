package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyListBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardListBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;
import java.util.Map;

/**
 * 卡业务处理
 * Created by Lily on 2017/8/21.
 */
public interface IBankCardApplyBizService {

    /**
     *
     * @param step
     * @return
     */
    ResultBean<List<BankCardApplyBean>> actGetBankCardApplyByCardStep(String step);

    /**
     * 客户签约提交后，通过监听来创建卡业务数据
     * @param orderId
     * @return
     */
    ResultBean<BankCardApplyBean> actCreateBankCardApply(String orderId);

    /**
     * 保存卡业务信息
     * @param bankCardApplyBean
     * @return
     */
    ResultBean<BankCardApplyBean> actSaveBankCardApply(BankCardApplyBean bankCardApplyBean);

    /**
     * 获取卡业务对象
     * @param id
     * @return
     */
    ResultBean<BankCardApplyBean> actFindBankCardApplyById(String id);

    /**
     *  根据交易ID，查询卡业务信息
     * @param transactionId
     * @return
     */
    ResultBean<BankCardApplyBean> actFindBankCardApplyByTransactionId(String transactionId);

    /**
     * 根据customerBean模糊查询
     * @param searchBean
     * @return
     */
    ResultBean<BankCardApplyBean> actSearchBankCardApply(SearchBean searchBean);

    /**
     * 通过签约单id获取卡业务对象
     * @param purchaseCarOrderId
     * @return
     */
    ResultBean<BankCardApplyBean> actFindBankCardApplyByPurchaseCarOrderId(String purchaseCarOrderId);

    /**
     * 启动卡业务流程
     * @return
     */
    ResultBean<BankCardApplyBean> actStartBankCardApply(BankCardApplyBean bankCardApplyBean);

    /**
     * 获取当前任务名称
     * @param id
     * @return
     */
    ResultBean<List<String>> actFindCurrentTask(String id);

    /**
     * 获取已经完成的任务
     * @param id
     * @return
     */
    ResultBean<List> actFindHistoryTask(String id);

    /**
     * 审批流任务完成
     * @param bankCardApplyBean
     * @param start
     * @return
     */
    ResultBean<BankCardApplyBean> actApprovedBankCardApply(BankCardApplyBean bankCardApplyBean,Integer approveStatus,Integer start,String loginUserId);

    /**
     * app审批流任务完成
     * @param bankCardBean
     * @param start
     * @return
     */
    ResultBean<BankCardBean> actApprovedBankCardApply(BankCardBean bankCardBean,Integer approveStatus,Integer start,String loginUserId);

    /**
     * 完成渠道领卡任务(监控预约刷卡渠道确认)
     * @param appointSwipindCardId
     */
    void actCompleteReceiveDiscount(String appointSwipindCardId);

    /**
     * 完成渠道刷卡任务(监控渠道刷卡确认)
     * @param swipindCardId
     */
    void actCompleteSwipingCardShop(String swipindCardId);

    /**
     * 查询卡业务的业务状态
     * @param transactionId
     * @return
     */
    ResultBean<Integer> actCheckBankCardApplyStatus(String transactionId);

    /**
     * 获取卡业务处理的列表
     * @param isPass
     * @param loginUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean<DataPageBean<BankCardApplyListBean>> actGetBankCards(Boolean isPass, String loginUserId, Integer pageIndex, Integer pageSize);

    /**
     * 代启卡失败时需要客户自启卡
     * @param id
     * @param approveStatus
     * @param initPassword
     * @return
     */
    ResultBean<BankCardApplyListBean> actSignBankCardByReplaceActivate(String id,Integer approveStatus,String initPassword,String loginUserId);

    /**
     * 重新制卡
     * @param bankCardId
     * @param comment
     * @param loginUserId
     * @return
     */
    ResultBean<BankCardApplyBean> actReMakeCard(String bankCardId,String comment,String loginUserId);


    void actDeleteBankCardApply(BankCardApplyBean bankCardApplyBean);

    /**
     * 计算某个月份的刷卡数据
     * @param beforeDate
     * @return
     */
    ResultBean<List<BankCardApplyBean>> getSwingCardBeforeEndDate(String beforeDate);

    /**
     * 获取日报数据信息
     */
    ResultBean<Map<Object,Object>> getDailyReport(String orgid,String date, BankCardApplyBean t);

    /**
     * 通过支行ID获取卡业务信息
     * @param searchBean
     * @return
     */
    ResultBean<DataPageBean<BankCardListBean>> actGetBankCardApplyListByCashSourceId(SearchBean searchBean);

    /**
     * 卡业务app详情页面展示
     * @param id
     * @return
     */
    ResultBean<BankCardBean> actGetBankCardApplyById(String id);

    /**
     * 领卡人
     * @param customerTransactionId
     * @param receiveCardName
     * @return
     */
    ResultBean<BankCardApplyBean> actsaveReceiveCardName(String customerTransactionId,String receiveCardName);

    ResultBean<List<String>> getDailySwipingMoneyTransactionIds(String date);

    /**
     * 通过已完成的任务获取下一步的任务
     * @param definitionKey
     * @return
     */
    ResultBean<List<BankCardListBean>> actGetBankCardApplyByTaskDefinitionKey(String definitionKey,String cashSourceId);

    /**
     *获取卡业务的状态,判断是否可以业务调整
     */
    ResultBean<String> actGetBankCardApplyApproveStatus(String transactionId);

    /**
     * 根据订单创建卡业务处理
     * @param
     * @return
     */
    ResultBean<BankCardApplyBean> actStartBankCardByTransaction(CustomerTransactionBean customerTransactionBean);

    /**
     *
     * 清除征信报告
     */
    ResultBean actDeleteReport(BankCardApplyBean bankCardApplyBean,String loginuserId);

    /**
     *获取销卡和已刷卡数据
     */
    ResultBean<List<BankCardApplyBean>> actGetByStatus();
}
