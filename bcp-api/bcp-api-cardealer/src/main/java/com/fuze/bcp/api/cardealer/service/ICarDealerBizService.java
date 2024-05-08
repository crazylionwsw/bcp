package com.fuze.bcp.api.cardealer.service;

import com.fuze.bcp.api.bd.bean.CarBrandBean;
import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.bean.DealerEmployeeBean;
import com.fuze.bcp.api.bd.bean.PayAccountBean;
import com.fuze.bcp.api.cardealer.bean.*;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.PayAccount;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/6/9.
 */
public interface ICarDealerBizService {

    /**
     * 保存 4S 店经销商信息
     *
     * @param carDealerBean
     * @return
     */
    ResultBean<CarDealerBean> actSaveCarDealer(CarDealerBean carDealerBean);

    ResultBean<CarDealerBean> actGetCarDealer(String id);

    ResultBean<List<CarDealerListBean>> actGetCarDealerQuery(String loginUserId, String inputStr);

    /**
     * 保存 4S 店经销商信息
     *
     * @param carDealerSubmissionBean
     * @return
     */
    ResultBean<CarDealerBean> actSaveCarDealerSubmission(CarDealerSubmissionBean carDealerSubmissionBean);

    /**
     * 删除 4S 店经销商信息
     *
     * @param id
     * @return
     */
    ResultBean<CarDealerBean> actDeleteCarDealer(@NotNull String id);

    /**
     * 根据ID 获取 4S 店经销商信息
     *
     * @param id
     * @return
     */
    ResultBean<CarDealerBean> actGetOneCarDealer(@NotNull String id);

    /**
     * 审查  审批
     *
     * @return
     */
    ResultBean<CarDealerBean> actSignCarDealer(String carDealerId, SignInfo signInfo);

    /**
     * 获取渠道合作支行
     *
     * @param id
     * @return
     */
    ResultBean<CashSourceBean> actFindCashSource(@NotNull String id);

    /**
     * 获取渠道合作支行
     *
     * @param id
     * @return
     */
    ResultBean<CashSourceBean> actFindCooperationCashSource(@NotNull String id);

    /**
     * 获取 4S 店经销商信息
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCarDealer();

    ResultBean<List<CarDealerBean>> actCarDealers();

    /**
     * 分页获取 4S 店经销商信息
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CarDealerBean>> actGetCarDealers(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取我的经销商
     *
     * @param loginUserId
     * @return
     */
    ResultBean<List<CarDealerListBean>> actGetMyCarDealers(String loginUserId);

    ResultBean<List<CarDealerListBean>> actGetMyCarDealers(String loginUserId, String ctype);

    /**
     * 检查经销商是否经营某品牌
     *
     * @param carDealerId
     * @param carTypeId
     * @return
     */
    ResultBean<Integer> actCheckDealerCarType(String carDealerId, String carTypeId);

    /**
     * 模糊查询
     *
     * @param carDealerBean
     * @return
     */
    ResultBean<CarDealerBean> actSearchCarDealers(Integer currentPage, CarDealerBean carDealerBean);

    /**
     * 获取经销商列表（无分页）
     *
     * @return
     */
    ResultBean<List<CarDealerBean>> actGetCarDealers();

    /**
     * 分页获取loginUserId 所属的 4S 店经销商信息
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CarDealerListBean>> actGetCarDealersPageByLoginUserId(Integer currentPage, Integer currentSize, String loginUserId);


    /**
     * 获取loginUserId 所属的 4S 店经销商信息, 分类型
     *
     * @param loginUserId
     * @param ctype       　ｏｃ　旧车　ｎｃ　新车
     * @return
     */
    ResultBean<List<CarDealerListBean>> actGetCarDealersByLoginUserId(@NotNull String loginUserId, @NotNull String ctype);

    /**
     * 保存 4S 店经销商员工信息
     *
     * @param dealerEmployeeBean
     * @return
     */
    ResultBean<DealerEmployeeBean> actSaveDealerEmployee(DealerEmployeeBean dealerEmployeeBean);

    /**
     * 删除 4S 店经销商员工信息
     *
     * @param id
     * @return
     */
    ResultBean<DealerEmployeeBean> actDeleteDealerEmployee(@NotNull String id);

    /**
     * 根据ID 获取4S 店经销商员工信息
     *
     * @param id
     * @return
     */
    ResultBean<DealerEmployeeBean> actGetOneDealerEmployee(@NotNull String id);

    /**
     * 获取 4S 店经销商员工信息
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupDealerEmployees();

    /**
     * 分页获取 4S 店经销商员工信息
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<DealerEmployeeBean>> actGetDealerEmployees(@NotNull @Min(0) Integer currentPage);


    /**
     * 获取指定4S店的员工列表
     *
     * @param currentPage
     * @param carDealerId
     * @return
     */
    ResultBean<DataPageBean<DealerEmployeeBean>> actGetDealerEmployees(@NotNull @Min(0) Integer currentPage, @NotNull String carDealerId);

    /**
     * 获取渠道的员工数量
     *
     * @param carDealerId
     * @return
     */
    ResultBean<Integer> actCountDealerEmployees(String carDealerId);

    /**
     * 获取指定4S店的全部员工列表
     *
     * @param carDealerId
     * @return
     */
    ResultBean<List<DealerEmployeeBean>> actGetAllDealerEmployees(@NotNull String carDealerId);

    /**
     * 根据ID获取4S店员工详情 包括 cardealername
     *
     * @param employeeId
     * @return
     */
    ResultBean<DealerEmployeeBean> actGetDealerEmployeeById(@NotNull String employeeId);

    /**
     * 保存渠道账号信息
     *
     * @param carDealerId
     * @return
     */
    ResultBean<PayAccount> actSaveCarDealerPayAccount(@NotNull String carDealerId, @NotNull Integer pindex, PayAccountBean payAccount);

    /**
     * 更新渠道的分期经理
     *
     * @param carDealerId
     * @return
     */
    ResultBean<CarDealerBean> actSetCarDealerBusinessMan(@NotNull String carDealerId, @NotNull String businessManId);

    /**
     * 更新渠道的分期经理
     *
     * @param carDealerId
     * @return
     */
    ResultBean<CarDealerBean> actSetCarDealerBusinessMans(@NotNull String carDealerId,List<String> businessManIds);


    /**
     * 删除指定渠道的某条账号信息
     *
     * @param carDealerId
     * @return
     */
    ResultBean<CarDealerBean> actDeleteCarDealerPayAccount(@NotNull String carDealerId, @NotNull Integer pindex);

    /**
     * 检查经销商的审核权限
     *
     * @param orgId
     * @param loginUserEmployeeId
     * @return
     */
    ResultBean<Boolean> actCheckAuditPermission(String orgId, String loginUserEmployeeId);

    /**
     * 查询经销商
     *
     * @param name
     * @param loginUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean<DataPageBean<CarDealerListBean>> actSearchCarDealers(String name, String loginUserId, int pageIndex, int pageSize);

    /**
     * 查找分组列表
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupDealerGroup();

    /**
     * 保存渠道分组
     *
     * @param dealerGroupBean
     * @return
     */
    ResultBean<DealerGroupBean> actSaveDealerGroup(DealerGroupBean dealerGroupBean);

    /**
     * 根据ID 获取分组
     *
     * @param id
     * @return
     */
    ResultBean<DealerGroupBean> actGetOneDealerGroup(@NotNull String id);

    /**
     * 删除渠道分组
     *
     * @param id
     * @return
     */
    ResultBean<DealerGroupBean> actDeleteDealerGroup(String id);

    /**
     * 获取渠道分组
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<DealerGroupBean>> actGetDealerGroups(Integer currentPage);

    /**
     * 保存返佣比例
     *
     * @param dealerSharingRatio
     * @return
     */
    ResultBean<DealerSharingRatioBean> actSaveDealerSharingRatio(DealerSharingRatioBean dealerSharingRatio);

    /**
     * 获取返佣比例
     *
     * @param carDealerId
     * @return
     */
    ResultBean<DealerSharingRatioBean> actGetDealerSharingRatio(String carDealerId);


    ResultBean<List<CarDealerBean>> actGetCarDealersByEmployeeIds(List<String> employeeIds);

    ResultBean<List<CarDealerBean>> actGetCarDealersByGroup(String groupId);

    ResultBean<DealerGroupBean> actGetDealerGroup( String id );

    //获取所有经销商员工
    ResultBean<DealerEmployeeBean> actGetAvaliableDealerEmployee();

    ResultBean<DealerEmployeeBean> actGetOneDealerEmployeeById(String id);

    ResultBean<Map<Object, Object>> actGetChannelCount(String date, String orginfoid, String employeeId);


    //获取多个经销商
    ResultBean<List<CarDealerBean>> actGetCarDealer(List<String> ids);


    ResultBean<List<CarDealerBean>> actGetCarDealerByOrgIds(List<String> orgIds);

    /**
     * 根据获取渠道下属的车辆品牌信息
     *
     * @param carDealerId
     * @return
     */
    ResultBean<CarBrandBean> actGetCarBrandByCardealer(@NotNull String carDealerId);

    void actDeleteCarDealerByIds(List<String> ids);

}
