package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.PoundageSettlementBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;
import java.util.Map;

/**
 * Created by zqw on 2017/8/29.
 */
public interface IPoundageSettlementBizService {

    /**
     *      获取  分页
     * @param currentPage
     * @return
     */
    ResultBean<PoundageSettlementBean> actGetPoundageSettlements(Integer currentPage);

    //  模糊查询
    ResultBean<PoundageSettlementBean> actSearchPoundageSettlements(int currentPage,String userName,String startTime,String endTime,PoundageSettlementBean poundageSettlementBean);

    /**
     *      根据  ID      获取
     * @param id
     * @return
     */
    ResultBean<PoundageSettlementBean> actGetOne(String id);

    /**
     *      根据  客户交易ID  查询
     * @param customerTransactionId
     * @return
     */
    ResultBean<PoundageSettlementBean> actGetOneByCustomerTransactionId(String customerTransactionId);

    /**
     *      保存   手续费    分润 记录
     * @param poundageSettlementBean
     * @return
     */
    ResultBean<PoundageSettlementBean> actSavePoundageSettlement(PoundageSettlementBean poundageSettlementBean);

    /**
     *      通过  客户交易ID      查询 该笔手续费分成的 营销代码
     * @param customerTransactionId
     * @return
     */
    ResultBean<PoundageSettlementBean> actGetMarketingCodeByCustomerTransactionId(String customerTransactionId);

    /**
     * 手续费分成计算
     * @param orderId
     * @return
     */
    ResultBean<PoundageSettlementBean> actCalculateFeeSharing(String orderId);

    /**
     * 手续费分成计算
     * @param transactionId  交易ID
     * @return
     */
    ResultBean<PoundageSettlementBean> actCalculateFeeSharingByTransactionId(String transactionId);

    //  计算报单行的每日手续费分成
    ResultBean<List<PoundageSettlementBean>>  actCalculateDailyDeclaration(PoundageSettlementBean poundageSettlementBean);

    //  计算渠道行的每日手续费分成
    ResultBean<List<PoundageSettlementBean>>  actCalculateDailyChannel(PoundageSettlementBean poundageSettlementBean);

    //  查询  截止某个日期累计发生的数据总和
    ResultBean<List<Map<String,Object>>>  actGetSummation(PoundageSettlementBean poundageSettlementBean);

    //  卡业务处理，刷卡完成，使分润数据生效
    void actUpdateEffectStatusByBankCardApplyId(String bankCardApplyId);

    //  根据取消业务单据ID，作废分润数据
    void actDiscardOneByCancelOrderId(String cancelOrderId);
}
