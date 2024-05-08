package com.fuze.bcp.api.bd.service;

import com.fuze.bcp.api.bd.bean.CarBrandBean;
import com.fuze.bcp.api.bd.bean.CarModelBean;
import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.api.bd.bean.CarTypeLookupBean;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by admin on 2017/6/9.
 */
public interface ICarTypeBizService {

    /****************************************** 车辆品牌信息 ******************************************/

    /**
     * 获取车辆品牌信息列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CarBrandBean>> actGetCarBrands(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取车辆品牌列表（不带分页）
     * @return
     */
    ResultBean<List<CarBrandBean>> actGetCarBrands();

    /**
     * 获取车辆品牌信息列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCarBrands();

    /**
     * 保存车辆品牌信息
     *
     * @param carBrandBean
     * @return
     */
    ResultBean<CarBrandBean> actSaveCarBrand(CarBrandBean carBrandBean);

    /**
     * 删除车辆品牌信息
     *
     * @param carBrandId
     * @return
     */
    ResultBean<CarBrandBean> actDeleteCarBrand(@NotNull String carBrandId);




    /**
     * 根据id获取车辆品牌信息
     *
     * @param id
     * @return
     */
    ResultBean<CarBrandBean> actGetCarBrandById(@NotNull String id);

    /****************************************** 车系 ******************************************/

    /**
     * 获取车系信息列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CarModelBean>> actGetCarModels(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取特定品牌车系信息列表（不带分页，返回所有数据）
     *
     * @param carBrandId
     * @return
     */
    ResultBean<List<CarModelBean>> actGetCarModels(@NotNull String carBrandId);

    /**
     * 获取特定品牌车系信息列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @param carBrandId
     * @return
     */
    ResultBean<DataPageBean<CarModelBean>> actGetCarModels(@NotNull @Min(0) Integer currentPage, @NotNull String carBrandId);

    /**
     * 获取车系列表，（只返回可用数据），建议使用有参方法
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCarModels();

    /**
     * 获取特定品牌车系列表，（只返回可用数据）
     *
     * @param carBrandId
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCarModels(@NotNull String carBrandId);

    /**
     * 保存车系信息
     *
     * @param carModelBean
     * @return
     */
    ResultBean<CarModelBean> actSaveCarModel(CarModelBean carModelBean);

    /**
     * 删除车系信息
     *
     * @param carModelId
     * @return
     */
    ResultBean<CarModelBean> actDeleteCarModel(@NotNull String carModelId);

    /**
     * 根据id获取车系信息
     *
     * @param id
     * @return
     */
    ResultBean<CarModelBean> actGetCarModelById(@NotNull String id);

    /****************************************** 车型信息 ******************************************/

    /**
     * 获取车型信息列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CarTypeBean>> actGetCarTypes(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取特定车系车型信息列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @param carModelId
     * @return
     */
    ResultBean<DataPageBean<CarTypeBean>> actGetCarTypes(@NotNull @Min(0) Integer currentPage, @NotNull String carModelId);

    /**
     * 获取特定车系车型信息列表（不带分页，返回所有数据）
     *
     * @param carModelId
     * @return
     */
    ResultBean<List<CarTypeBean>> actGetCarTypes(@NotNull String carModelId);

    /**
     * 获取车型列表，（只返回可用数据），建议使用有参方法
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCarTypes();

    /**
     * 获取特定车系车型列表，（只返回可用数据）
     *
     * @param carModelId
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCarTypes(@NotNull String carModelId);

    /**
     * 获取指定车系下的所有车型（只返回可用数据）
     * @param carModelIds
     * @return
     */
    ResultBean<List<CarTypeLookupBean>> actLookupCarTypes(List<String> carModelIds);


    /**
     * 保存车型信息
     *
     * @param carTypeBean
     * @return
     */
    ResultBean<CarTypeBean> actSaveCarType(CarTypeBean carTypeBean);

    /**
     * 删除车型信息
     *
     * @param carTypeId
     * @return
     */
    ResultBean<CarTypeBean> actDeleteCarType(@NotNull String carTypeId);

    /**
     * 根据id获取车型信息
     *
     * @param id
     * @return
     */
    ResultBean<CarTypeBean> actGetCarTypeById(@NotNull String id);

    /**
     * 根据品牌获取车型
     * @param carBrandId
     * @return
     */
    ResultBean<List<CarTypeBean>> actGetCarTypeByCarBrand(@NotNull String carBrandId);


    /**
     * 同步某一品牌的车系、车型等数据
     * @param carBrandId
     * @return
     */
    ResultBean<String> actSyncCarBrand(String carBrandId, String userId) throws Exception;

    /**
     * 根据外部ID获取车型
     * @param refStyleId
     * @return
     */
    ResultBean<CarTypeBean> actGetCarTypeByRefStyleId(String refStyleId);

    /**
     * 查询品牌
     * @param refMakeId
     * @return
     */
    ResultBean<CarBrandBean> actGetCarBrandByRefMakeId(String refMakeId);

    /**
     * 查询品牌
     * @param refModelId
     * @return
     */
    ResultBean<CarModelBean> actGetCarModelByRefModelId(String refModelId);

    ResultBean<List<CarBrandBean>> actGetCarBrandAll();

    ResultBean<List<CarBrandBean>> actGetCarBrandList(List<String> carBrandIds);

}
