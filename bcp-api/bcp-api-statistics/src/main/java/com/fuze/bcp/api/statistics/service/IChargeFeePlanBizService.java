package com.fuze.bcp.api.statistics.service;

import com.fuze.bcp.api.statistics.bean.ChargeFeePlanBean;

import com.fuze.bcp.api.statistics.bean.ChargeFeePlanDetailBean;
import com.fuze.bcp.api.statistics.bean.ChargeFeePlanErrorExport;
import com.fuze.bcp.api.statistics.bean.QueryFilter;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;


import java.io.OutputStream;
import java.util.List;

/**
 * Created by GQR on 2017/10/23.
 */
public interface IChargeFeePlanBizService {


    /**
     * 同步审核通过的订单收款计划表
     * @return
     */
    ResultBean<String> actSyncAllOrders();

    /**
     * 根据某个日期月份年的刷卡数据，计算收款计划表
     * @param date
     * @return
     */
    ResultBean<String> actSyncSwingCard(String date,SignInfo signInfo);

    /**
     * 根据id查找detail
     */
    ResultBean<List<ChargeFeePlanDetailBean>> actFindOneDetailById(String chargeFeePlanId);

    /**
     * 查找所有的detail
     */
    ResultBean<List<ChargeFeePlanDetailBean>> actFindAllDetail();

    /**
     * 根据id浏览详细信息
     * @param id
     * @return
     */
    ResultBean<ChargeFeePlanBean> actGetChargeFeePlanById(String id);

    /**
     * 根据状态查找错误数据信息
     * @param status
     * @return
     */
    ResultBean<List<ChargeFeePlanBean>> actFindErrorStatus(Integer status);


    /**
     * 监控签约提交时创建收款计划表
     * @param customerTransactionId
     * @return
     */
   ResultBean<ChargeFeePlanBean> actCreatePlan(String customerTransactionId) ;


    /**
     * 重算
     * @param id
     * @return
     */
   ResultBean<ChargeFeePlanBean> actRecreateChargeFeePlan(String id,SignInfo signInfo);

    /**
     * 核对还款计划表
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<ChargeFeePlanBean> actCheckOne(String id,String LoginUserId, SignInfo signInfo);

    /**
     * 核对多个还款计划
     * @param ids
     * @param LoginUserId
     * @param signInfo
     * @return
     */
    ResultBean<String> actCheck(List<String> ids,String LoginUserId, SignInfo signInfo);

    /**
     * 取消核对的还款计划表
     * @param id
     * @return
     */
    ResultBean<ChargeFeePlanBean> actUncheckOne(String id,SignInfo signInfo);

    /**
     * 取消核对多个还款信息
     * @param choseAll
     * @param signInfo
     * @return
     */
    ResultBean<String> actUncheck(List<String> choseAll,SignInfo signInfo);

    /**
     * 导出错误的收款计划表
     * @param
     * @return
     */
    ResultBean<List<ChargeFeePlanErrorExport>> actExportFailedExcel();

    /**
     * 按照月份计算收款计划
     * @param beforeDate
     * @return
     */
    ResultBean<List<ChargeFeePlanBean>> actSyncAllBankCard(String beforeDate);

    /**
     * 查询  /模糊查询 （升级后）
     * @param searchBean
     * @return
     */
    ResultBean<ChargeFeePlanBean>  actSearchChargeFeePlans(SearchBean searchBean);

    /*
     * 查找错误数据  判断是否为空
    * */
     ResultBean<List<ChargeFeePlanBean>> actFindFailedExcel();

    /**
     * 根据交易Id查询
     * @param transactionId
     * @return
     */
    ResultBean<ChargeFeePlanBean> actChargeFeePlan(String transactionId);

    /**
     * 保存
     * @param chargeFeePlanBean
     * @return
     */
    ResultBean<ChargeFeePlanBean> actSaveChargeFeePlan(ChargeFeePlanBean chargeFeePlanBean);




}
