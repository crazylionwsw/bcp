package com.fuze.bcp.api.bd.service;

import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.bean.CashSourceEmployeeBean;
import com.fuze.bcp.api.bd.bean.SourceRateBean;
import com.fuze.bcp.api.bd.bean.SourceRateLookupBean;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.APITreeLookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/6/9.
 */
public interface ICashSourceBizService {
    /**
     * 资金提供方类型
     * @return
     */
    ResultBean<List> actGetAllCashSourceType();

    /**
     * 获取全部资金提供方
     * @return
     */
    ResultBean<List<CashSourceBean>> actGetCashSources();

    /**
     * 获取全部报单行
     * @return
     */
    ResultBean<List<CashSourceBean>> actGetCashSourceBanks();

    /**
     * 添加贷款人
     *
     * @param cashSourceBean
     * @return
     */

    ResultBean<CashSourceBean> actSaveCashSource(CashSourceBean cashSourceBean);

    /**
     * 删除贷款人
     *
     * @param cashSourceId
     * @return
     */
    ResultBean<CashSourceBean> actDeleteCashSource(@NotNull String cashSourceId);

    /**
     * 获取可用贷款人列表
     *
     * @return
     */
    ResultBean<List<APITreeLookupBean>> actLookupCashSources();

    /**
     * 分页获取贷款人列表
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CashSourceBean>> actGetCashSources(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取下级货款人列表
     *
     * @param parentId
     * @return
     */
    ResultBean<List<CashSourceBean>> actGetCashSources(@NotNull String parentId);

    /**
     * 根据ID获取贷款人
     *
     * @param id
     * @return
     */
    ResultBean<CashSourceBean> actGetCashSource(@NotNull String id);


    /**
     * 保存资金提供方信息
     *
     * @param cashSourceEmployeeBean
     * @return
     */
    ResultBean<CashSourceEmployeeBean> actSaveCashSourceEmployee(CashSourceEmployeeBean cashSourceEmployeeBean);

    /**
     * 删除资金提供方信息
     *
     * @param cashSourceEmployeeId
     * @return
     */
    ResultBean<CashSourceEmployeeBean> actDeleteCashSourceEmployee(@NotNull String cashSourceEmployeeId);

    /**
     * //获取所有协作用户
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCashSourceEmployees();

    /**
     * 获取资金提供方分页信息
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CashSourceEmployeeBean>> actGetCashSourceEmployees(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取指定资金机构的员工
     *
     * @param cashSourceId
     * @return
     */

    ResultBean<List<CashSourceEmployeeBean>> actGetCashSourceEmployees(@NotNull String cashSourceId);


    /**
     * 根据ID获取资金提供方
     *
     * @param id
     * @return
     */
    ResultBean<CashSourceEmployeeBean> actGetCashSourceEmployee(@NotNull String id);

    /**
     * 保存资金利率
     *
     * @param sourceRateBean
     * @return
     */
    ResultBean<SourceRateBean> actSaveSourceRate(SourceRateBean sourceRateBean);

    /**
     * 删除资金利率
     *
     * @param sourceRateId
     * @return
     */
    ResultBean<SourceRateBean> actDeleteSourceRate(@NotNull String sourceRateId);

    /**
     * 获取资金利率列表
     *
     * @return
     */
    ResultBean<List<SourceRateLookupBean>> actLookupSourceRates();


    /**
     * 分页获取资金利率列表
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<SourceRateBean>> actGetSourceRates(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取指定资金机构的资金利率列表
     *
     * @param cashSourceId
     * @return
     */

    ResultBean<List<SourceRateBean>> actGetSourceRates(@NotNull String cashSourceId);

    /**
     * 根据ID获取资金利率
     *
     * @param id
     * @return
     */
    ResultBean<SourceRateBean> actGetSourceRate(@NotNull String id);

    /**
     * 根据分行编码获取所有的支行信息
     * @param branchCode
     * @return
     */
    ResultBean<List<CashSourceBean>> actFindChildBank(String branchCode);

    //获取所有可用银行协作员工
    ResultBean<List<CashSourceEmployeeBean>> actGetAvaliableCashSourceEmployee();

}
