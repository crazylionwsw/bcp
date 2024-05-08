package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentExcelBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentListBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentSubmissionBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;
import java.util.Map;

/**
 * 预约垫资的服务接口
 */
public interface IAppointPaymentBizService {

    /**
     * 客户签约提交后，通过监听来创建预约垫资单
     * @param orderId
     * @return
     */
    ResultBean<AppointPaymentBean> actCreateAppointPayment(String orderId);

    /**
     * 非贴息的情况下计算垫资额
     * @param tid
     * @param chargeParty
     * @return
     */
    ResultBean<Double> actGetAppointPaymentAmount(String tid, String chargeParty);

    /**
     * 预约刷卡代垫贴息额，创建预约垫资单
     * @param swipingCardId
     * @return
     */
    ResultBean<AppointPaymentBean> actCreateAppointPaymentByswipingCardId(String swipingCardId);

    /**
     * 保存预约垫资单 （暂存，不进审批流）
     * @param appointPaymentSubmissionBean
     * @return
     */
    ResultBean<AppointPaymentBean> actSaveAppointPayment(AppointPaymentSubmissionBean appointPaymentSubmissionBean);

    //保存垫资信息
    ResultBean<AppointPaymentBean> actSaveAppointPaymentInfo(AppointPaymentBean appointPaymentBean);

    /**
     * 保存PAD端提交的预约垫资单 （数据转换）
     * @param appointPaymentSubmissionBean
     * @return
     */
    ResultBean<AppointPaymentBean> actSavePadAppointPayment(AppointPaymentSubmissionBean appointPaymentSubmissionBean);

    /**
     * 提交预约垫资单 （保存，并进审批流）
     * @param id
     * @return
     */
    ResultBean<AppointPaymentBean> actSubmitAppointPayment(String id,String comment);

    /**
     * 获取分期经理的预约垫资单
     * @param isPass
     * @param loginUserId 分期经理的用户ID
     * @param currentPage
     * @return
     */
    ResultBean<List<AppointPaymentListBean>> actGetAppointPayments(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize);

    /**
     * 根据交易ID获取详情数据
     * @param transactionId
     * @return
     */
    ResultBean<AppointPaymentSubmissionBean> actInitAppointPaymentsByTransactionId(String transactionId);

    /**
     * 获取预约垫资单
     * @param id
     * @return
     */
    ResultBean<AppointPaymentBean> actGetAppointPayment(String id);

    /**
     * 审核
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<AppointPaymentBean> actSignAppointPayment(String id, SignInfo signInfo);

    /**
     * 预约垫资查询（升级后）
     * @param searchBean
     * @return
     */
    ResultBean<AppointPaymentBean> actSearchAppointPayments(String userId, SearchBean searchBean);

    /**
     *  更新业务状态
     * @param id
     * @param status
     * @return
     */
    ResultBean<AppointPaymentBean> actUpdateStatus(String id, Integer status);

    /**
     * 根据交易ID获取
     */
    ResultBean<AppointPaymentBean> actGetAppointPaymentByCustomerTransactionId(String customerTransactionId);

    /**
     * 获取日报数据信息
     */
    ResultBean<Map<Object,Object>> getDailyReport(String orgid,String date, AppointPaymentBean t);
    /**
     * 获取分期经理报表
     */
    ResultBean<Map<Object,Object>> getEmployeeReport(String date, AppointPaymentBean t, String employeeId);

    /**
     * 获取渠道经理报表
     */
    ResultBean<Map<Object,Object>> getChannelReport(String date, AppointPaymentBean t,String employeeId);
    /**
     * 获取统计数据
     * @param as
     * @return
     */
    ResultBean<List<Map>> getAllCustomerByApproveStatus(Integer as);

    //  发送预约垫资邮件，附件为预约垫资电子回单
    void actSendAppointPaymentVoucher(String id);

    /**
     * 获取所有的
     * @return
     */
    ResultBean<List<AppointPaymentBean>> actGetCustomerTransactionsByCarDealerId(String carDealerId);

    /**
     * 财务台账导出
     */
    ResultBean<List<AppointPaymentExcelBean>> actExportAppointPayBusinessBook(String selectTime);
}
