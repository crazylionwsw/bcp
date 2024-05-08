package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferBean;
import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferListBean;
import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferSubmissionBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;
import java.util.Map;

/**
 * 转移过户
 * Created by Lily on 2017/9/14.
 */
public interface ICarTransferBizService {
    /**
     * 创建转移过户单
     * @param orderId
     * @return
     */
    ResultBean<CarTransferBean> actCreateCarTransfer(String orderId);

    /**
     * 保存转移过户单 （暂存）
     * @param carTransferSubmissionBean
     * @return
     */
    ResultBean<CarTransferSubmissionBean> actSaveCarTransfer(CarTransferSubmissionBean carTransferSubmissionBean);

    /**
     * 提交转移过户单  （保存并进审批流）
     * @param id
     * @return
     */
    ResultBean<CarTransferBean> actSubmitCarTransfer(String id,String comment);

    /**
     * 获取分期经理的转移过户单
     * @param isPass
     * @param loginUserId 分期经理的用户ID
     * @param currentPage
     * @return
     */
    ResultBean<List<CarTransferListBean>> actGetCarTransfers(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize);

    /**
     * 根据交易ID获取详情数据
     * @param transactionId
     * @return
     */
    ResultBean<CarTransferSubmissionBean> actInitCarTransferByTransactionId(String transactionId);

    /**
     *      根据ID回显
     * @param id
     * @return
     */
    ResultBean<CarTransferBean> actGetCarTransfer(String id);

    /**
     *  保存
     * @param carTransferBean
     * @return
     */
    ResultBean<CarTransferBean> actSaveCarTransfer(CarTransferBean carTransferBean);

    /**
     * 转移过户  模糊查询（升级后）
     * @param searchBean
     * @return
     */
    ResultBean<CarTransferBean> actSearchCarTransfers(String userId, SearchBean searchBean);

    /**
     *  根据交易ID回显
     * @param customerTransactionId
     * @return
     */
    ResultBean<CarTransferBean> actGetByCustomerTransactionId(String customerTransactionId);

    /**
     * 签批
     *
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<CarTransferBean> actSignCarTransfer(String id, SignInfo signInfo);

    /**
     * 获取转移过户日报数据信息
     */
    ResultBean<Map<Object,Object>> getDailyReport(String orgid,String date, CarTransferBean t);

    ResultBean<Map<Object, Object>> getEmployeeReport(String employeeId, String date, CarTransferBean t);

}
