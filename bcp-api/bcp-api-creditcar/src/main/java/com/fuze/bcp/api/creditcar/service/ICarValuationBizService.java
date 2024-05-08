package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.api.creditcar.bean.CarValuationBean;
import com.fuze.bcp.api.creditcar.bean.ValuationListBean;
import com.fuze.bcp.api.creditcar.bean.ValuationSubmissionBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/8/14.
 */
public interface ICarValuationBizService {

    /**
     * 根据渠道查询所有的车辆评估历史记录
     * @return
     */
    ResultBean<List<ValuationListBean>> actFindCarValuationsByCarDealerId(String carDealerId);

    /**
     * 根据渠道查询所有的车辆评估单(含本身)
     * 业务调整时用此方法
     */
    ResultBean<List<ValuationListBean>> actGetCarValuationsOnBusinessExchange(String carDealerId,String vehicleEvaluateInfoId);

    /**
     * 获取评估列表（带分页）
     * @param page
     * @return
     */
    ResultBean<List<ValuationListBean>> actGetValuations(Integer page);

    /**
     * 获取评估列表（带分页）
     * @param pageindex
     * @param pagesize
     * @return
     */
    ResultBean<DataPageBean<ValuationListBean>> actGetValuations(Integer pageindex, Integer pagesize);

    /**
     * 提交待评估车辆信息，进入工作流
     * @param valuation
     * @return
     */
    ResultBean<String> actSubmitValuation(ValuationSubmissionBean valuation);

    /**
     * 通过VIN码查询某车辆的评估单
     * @param carTypeId
     * @param vin
     * @return
     */
    ResultBean<CarValuationBean> actFindValuationByVin(String carTypeId, String vin);

    /**
     * 通过VIN码查询某车辆的评估单
     * @param vin
     * @return
     */
    ResultBean<CarValuationBean> actGetValuationByVin(String vin);

    /**
     * 通过ID获取评估单的详情
     * @param id
     * @return
     */
    ResultBean<ValuationSubmissionBean> actFindValuationById(String id);


    /**
     * 通过ID获取评估历史记录
     * @param id
     * @return
     */
    ResultBean<CarValuationBean> actFindCarValuationById(String id);


    /**
     * 签约时使用评估单
     * @param valuation
     * @param customerId
     * @param transactionId
     * @param orderId
     * @return
     */
    ResultBean actUseValuation(CarValuationBean valuation, String customerId, String transactionId, String orderId);

    /**
     * 取消使用某评估单
     * @param valuation
     * @return
     */
    ResultBean actUnuseValuation(CarValuationBean valuation);

    //获取评估单信息(带分页)
    ResultBean<DataPageBean<CarValuationBean>> actGetCarValuations(@NotNull @Min(0) Integer currentPage,int approveStatus);

    //保存或更新评估信息
    ResultBean<CarValuationBean> actSaveCarValuation(CarValuationBean carValuationBean);

    //评估确认
    ResultBean<CarValuationBean> actSavePassCarValuation(CarValuationBean carValuationBean);

    //评估取消
    ResultBean<CarValuationBean> actCancelCarValuation(CarValuationBean carValuationBean);

    //删除评估信息
    ResultBean<CarValuationBean> actDeleteCarValuation(@NotNull String id);

    //查询评估信息
    ResultBean<DataPageBean<CarValuationBean>> actSearchCarValuations(Integer currentPage,CarValuationBean carValuationBean);

    //判断评估单是否被签约使用
    ResultBean<Boolean> actGetCarValuationFinshOrder(String carValuationId);

    //通过cardealerId查询
    ResultBean<List<CarValuationBean>> actGetCarValuation(String carDealerId);

}
