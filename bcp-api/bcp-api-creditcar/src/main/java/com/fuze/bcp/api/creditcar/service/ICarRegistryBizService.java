package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistryBean;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistryListBean;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistrySubmissionBean;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;

import java.util.List;
import java.util.Map;

/**
 * Created by GQR on 2017/8/19.
 * 提车记录
 */
public interface ICarRegistryBizService {

    /**
     * 获取日报数据信息
     */
    ResultBean<Map<Object,Object>> getDailyReport(String orgid,String date, CarRegistryBean t);

    /**
     * 根据登录用户的员工ID（employeeId）,获取获取分期经理报表
     * @param date
     * @param t
     * @param employeeId
     * @return
     */
    ResultBean<Map<Object,Object>> getEmployeeReport(String employeeId, String date, CarRegistryBean t);

    /**
     * 创建车辆上牌(监控垫资支付)
     * @param paymentId
     * @return
     */
    ResultBean<CarRegistryBean> actCreateCarRegistry(String paymentId);

    /**
     * 创建车辆上牌(监控渠道刷卡)
     * @param swipingCardId
     * @return
     */
    ResultBean<CarRegistryBean> actCreateCarRegistryBySwipingCardId(String swipingCardId);

    /**
     * 保存车辆上牌 （暂存）
     *
     * @param carRegistrySubmissionBean
     * @return
     */
    ResultBean<CarRegistrySubmissionBean> actSaveCarRegistry(CarRegistrySubmissionBean carRegistrySubmissionBean);


    /**
     * 提交车辆上牌  （保存并进审批流）
     *
     * @param id
     * @return
     */
    ResultBean<CarRegistryBean> actSubmitCarRegistry(String id, String comment);

    /**
     * 获取分期经理的车辆上牌列表
     * @param isPass
     * @param loginUserId 分期经理的用户ID
     * @param currentPage
     * @return
     */
    ResultBean<List<CarRegistryListBean>> actGetCarRegistrys(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize);

    /**
     * 根据交易ID获取详情数据
     *
     * @param transactionId
     * @return
     */
    ResultBean<CarRegistrySubmissionBean> actInitCarRegistryByTransactionId(String transactionId);

    /**
     * 根据ID    回显
     *
     * @param id
     * @return
     */
    ResultBean<CarRegistryBean> actGetCarRegistry(String id);

    /**
     * 提车上牌，模糊查询（升级后）
     * @param searchBean
     * @return
     */
    ResultBean<CarRegistryBean> actSearchCarRegistries(String userId, SearchBean searchBean);

    /**
     * 保存 提车记录
     *
     * @param carRegistryBean
     * @return
     */
    ResultBean<CarRegistryBean> actSaveCarRegistry(CarRegistryBean carRegistryBean);

    /**
     * 签批
     *
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<CarRegistryBean> actSignCarRegistry(String id, SignInfo signInfo);

    /**
     * 根据 客户交易ID   查询提车记录
     *
     * @param customerTransactionId
     * @return
     */
    ResultBean<CarRegistryBean> actGetByCustomerTransactionId(String customerTransactionId);

    ResultBean<CarRegistryBean> actGetByCustomer(CustomerBean customerBean);
}
