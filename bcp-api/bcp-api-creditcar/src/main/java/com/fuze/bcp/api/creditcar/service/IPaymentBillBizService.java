package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.paymentBill.PaymentBillBean;
import com.fuze.bcp.api.creditcar.bean.paymentBill.PaymentBillListBean;
import com.fuze.bcp.api.creditcar.bean.paymentBill.PaymentBillSubmissionBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/5.
 */
public interface IPaymentBillBizService {

    /**
     *创建缴费单(PAD端)
     */
    ResultBean<PaymentBillSubmissionBean> actCreatePaymentBill(String transactionId);

    /**
     * 保存缴费单
     */
    ResultBean<PaymentBillSubmissionBean> actSavePaymentBill(PaymentBillSubmissionBean paymentBillSubmissionBean);

    /**
     * 提交缴费单(PAD端)
     */
    ResultBean<PaymentBillSubmissionBean> actSubmitPaymentBill(PaymentBillSubmissionBean paymentBillSubmissionBean);

    /**
     * 获取分期经理缴费单列表(PAD端)
     */
    ResultBean<List<PaymentBillListBean>> actGetPaymentBills(Boolean isPass, String loginUserId, Integer pageIndex, Integer pageSize);

    /**
     *获取缴费单详情(PAD端)
     */
    ResultBean<PaymentBillBean> actGetPaymentBillInfo(String paymentBillId);

    /**
     * 查询该笔交易有几笔缴费单
     */
    ResultBean<Integer> actGetPaymentBillCount(String transactionId);

    /**
     * 根据交易id查询所有缴费单（多条）
     * @param transactionId
     * @return
     */
    ResultBean<PaymentBillBean> actGetPaymentBillsByTransactionId(String transactionId);

    /**
     *获取缴费单详情
     */
    ResultBean<PaymentBillBean> actGetPaymentBill(String paymentBillId);


    /**
     *获取缴费单列表(含查询)
     */
    ResultBean<PaymentBillBean> actSearchPaymentBill(String userId, SearchBean searchBean);


    /**
     * 审核缴费单
     */
    ResultBean<PaymentBillBean> actSignPaymentBill(String paymentBillId, SignInfo signInfo);

    /**
     * 解押时创建缴费单(包括Pc和Pad)
     */
    ResultBean<PaymentBillBean> actCreatePaymentBillByDecompress(String transactionId,String userId,String type);



}
