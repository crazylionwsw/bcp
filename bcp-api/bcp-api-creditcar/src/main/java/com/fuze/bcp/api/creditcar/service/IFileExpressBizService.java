package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.FileExpressBean;
import com.fuze.bcp.api.creditcar.bean.fileexpress.FileExpressListBean;
import com.fuze.bcp.api.creditcar.bean.fileexpress.FileExpressSubmissionBean;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * 快递单的服务接口
 */
public interface IFileExpressBizService {

    /**
     * 创建快递单
     * @param orderId
     * @return
     */
    ResultBean<FileExpressBean> actFileExpress(String orderId);

    /**
     * 保存快递单 （暂存）
     * @param fileExpressSubmissionBean
     * @return
     */
    ResultBean<FileExpressSubmissionBean> actSaveFileExpress(FileExpressSubmissionBean fileExpressSubmissionBean);


    /**
     * 提交快递单  （保存并进审批流）
     * @param id
     * @return
     */
    ResultBean<FileExpressBean> actSubmitFileExpress(String id, String comment);

    /**
     * 获取分期经理的快递单
     * @param loginUserId 分期经理的用户ID
     * @param currentPage
     * @return
     */
    ResultBean<List<FileExpressListBean>> actGetFileExpress(String loginUserId, Integer currentPage, Integer currentSize);

    /**
     * 根据交易ID获取详情数据
     * @param transactionId
     * @return
     */
    ResultBean<FileExpressSubmissionBean> actInitFileExpressByTransactionId(String transactionId);

    /**
     * 获取快递单
     * @param id
     * @return
     */
    ResultBean<FileExpressBean> actGetFileExpress(String id);

    /**
     * 确认收到快递
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<FileExpressBean> actConfirmFileExpress(String id, SignInfo signInfo);


    /**
     * 快递单查询列表
     * @param currentPage
     * @return
     */
    ResultBean<FileExpressBean> actGetFileExpresss(int currentPage);



    /**
     *  模糊查询 快递单
     * @param currentPage
     * @param customerBean
     * @return
     */
    ResultBean<FileExpressBean> actSearchFileExpresss(int currentPage, CustomerBean customerBean);


}
