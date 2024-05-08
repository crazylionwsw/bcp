package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.CustomerDemandBean;
import com.fuze.bcp.api.creditcar.bean.PickupCarBean;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;

import java.util.Map;

/**
 * Created by GQR on 2017/8/19.
 * 提车记录
 */
public interface IPickupCarBizService {

    /**
     * 获取日报数据信息
     */
    ResultBean<Map<Object,Object>> getDailyReport(String orgid,String date, PickupCarBean t);
    /**
     * 获取提车记录列表
     * @param currentPage
     * @return
     */
    ResultBean<PickupCarBean> actGetPickupCars(int currentPage);

    /**
     * 获取提车记录列表
     * @param
     * @return
     */
    ResultBean<PickupCarBean> getPickupCarsByCustomer(CustomerBean customer, Integer status);

    /**
     *          根据ID    回显
     * @param id
     * @return
     */
    ResultBean<PickupCarBean> actGetPickupCar(String id);

    /**
     *      模糊查询
     * @param currentPage
     * @param customerBean
     * @return
     */
    ResultBean<PickupCarBean> actSearchPickupCars(int currentPage, CustomerBean customerBean);

    /**
     *  保存 提车记录
     * @param pickupCarBean
     * @return
     */
    ResultBean<PickupCarBean> actSavePickupCar(PickupCarBean pickupCarBean);

    /**
     *      签批
     * @param id
     * @param signInfo
     * @return
     */
    ResultBean<PickupCarBean> actSignPickupCar(String id, SignInfo signInfo);

    /**
     *      根据 客户交易ID   查询提车记录
     * @param customerTransactionId
     * @return
     */
    ResultBean<PickupCarBean> actGetByCustomerTransactionId(String customerTransactionId);
}
