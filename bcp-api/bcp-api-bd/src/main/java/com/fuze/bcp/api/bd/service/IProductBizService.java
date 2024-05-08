package com.fuze.bcp.api.bd.service;

import com.fuze.bcp.api.bd.bean.*;
import com.fuze.bcp.api.drools.bean.AccrualSubsidiesBean;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.PadSalesRate;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by admin on 2017/6/9.
 */
public interface IProductBizService {
    /****************************************** 分期产品维护 ******************************************/
    /**
     * 获取分期产品列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CreditProductBean>> actGetCreditProducts(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取分期产品列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCreditProducts();

    /**
     * 保存分期产品
     *
     * @param creditProduct
     * @return
     */
    ResultBean<CreditProductBean> actSaveCreditProduct(CreditProductBean creditProduct);

    /**
     * 删除分期产品
     *
     * @param creditProductId
     * @return
     */
    ResultBean<CreditProductBean> actDeleteCreditProduct(@NotNull String creditProductId);

    /**
     *  获取分期产品列表 （不带分页）
     * @return
     */
    ResultBean<List<CreditProductBean>> actGetCreditProducts();

    /**
     * 获取单条数据
     * @param creditProductId
     * @return
     */
    ResultBean<CreditProductBean> actGetCreditProduct(String creditProductId);


    /******************************贴息产品内容*************************************************/
    /**
     * 获取所有贴息数据(带分页)
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CompensatoryPolicyBean>> actGetCompensatoryPolicies(@NotNull@Min(0) Integer currentPage);

    /**
     * 保存贴息数据
     * @param compensatoryPolicyBean
     * @return
     */
    ResultBean<CompensatoryPolicyBean> actSaveCompensatoryPolicy(CompensatoryPolicyBean compensatoryPolicyBean);

    /**
     * 删除贴息数据
     * @param compensatorypolicyId
     * @return
     */
    ResultBean<CompensatoryPolicyBean> actDeleteCompensatoryPolicy(@NotNull String compensatorypolicyId);

    /**
     * 获取所有贴息数据(仅可用)
     * @return
     */
    ResultBean<APILookupBean> actLookupCompensatoryPolicies();

    /**
     * 获取单条贴息数据
     * @param compensatorypolicyId
     * @return
     */
    ResultBean<CompensatoryPolicyBean> actGetCompensatoryPolicy(String compensatorypolicyId);


    /**
     * 根据厂商和当前日期获取贴息政策
     * @param carBrandId
     * @param date
     * @return
     */
    ResultBean<List<CompensatoryPolicyBean>> actGetCompensatoryPolicy(String carBrandId, String date);

    /**
     *
     * 分页获取厂商和当前日期贴息政策
     *
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean<DataPageBean<CompensatoryPolicyListBean>> actGetCompensatoryPolicyPage(String date, Integer pageIndex, Integer pageSize);

    /**
     * 计算贴息额
     * @param accrual
     * @return
     */
    ResultBean<AccrualSubsidiesBean> actGetCompensatory(AccrualSubsidiesBean accrual);


    /**
     * 贴息政策列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean<DataPageBean<CompensatoryPolicyListBean>> actGetCompensatoryPolicies(Integer pageIndex, Integer pageSize);


    /**
     * 分页贴息公式
     */
    ResultBean<DataPageBean<CompensatoryPolicyFormulaBean>> actGetFormulas(Integer currentPage);

    /**
     * 贴息公式列表
     */
    ResultBean<List<CompensatoryPolicyFormulaBean>> actGetFormulas();

    /**
     * 新增/编辑贴息公式保存
     */
    ResultBean<CompensatoryPolicyFormulaBean> actSaveFormula(CompensatoryPolicyFormulaBean compensatoryPolicyFormulaBean);

    /**
     * 删除
     * @param formulaId
     * @return
     */
    ResultBean<CompensatoryPolicyFormulaBean> actDeleteFormula(String formulaId);


    ResultBean<CompensatoryPolicyFormulaBean> actGetFormula(String id);

    /*************************************促销管理**********************************************************/
    /**
     * 获取促销数据(带分页)
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<PromotionPolicyBean>> actGetPromotionPolicies(@NotNull@Min(0) Integer currentPage);

    /**
     * 保存促销数据
     * @param salesPolicy
     * @return
     */
    ResultBean<PromotionPolicyBean> actSavePromotionPolicy(PromotionPolicyBean salesPolicy);

    /**
     * 删除促销数据
     * @param salespolicyId
     * @return
     */
    ResultBean<PromotionPolicyBean> actDeletePromotionPolicy(@NotNull String salespolicyId);

    /**
     * 获取促销数据(仅可用)
     * @return
     */
    ResultBean<APILookupBean> actLookupPromotionPolicies();




    /************************************************************销售政策管理**************************************************************/
    /**
     * 获取销售政策数据
     * @return
     */
    ResultBean<List<SalesPolicyBean>> actGetSalesPolicies();

    /**
     * 保存销售政策数据
     * @param salesPolicy
     * @return
     */
    ResultBean<SalesPolicyBean> actSaveSalesPolicy(SalesPolicyBean salesPolicy);

    /**
     * 删除销售政策数据
     * @param salespolicyId
     * @return
     */
    ResultBean<SalesPolicyBean> actDeleteSalesPolicy(@NotNull String salespolicyId);

    /**
     * 获取销售政策数据(仅可用)
     * @return
     */
    ResultBean<APILookupBean> actLookupSalesPolicies();

    /**
     * 获取销售政策数据(全部)
     * @return
     */
    ResultBean<List<SalesPolicyBean>> actSalesPolicys();

    /**
     * 获取PAD销售政策数据(全部)
     * @return
     */
    ResultBean<List<PadSalesPolicyBean>> actGetPadSalesPolicys();

    /**
     * 根据品牌id获取销售政策的内容
     * @param carBrandId
     * @return
     */
    ResultBean<List<CompensatoryPolicyBean>> actGetCompensatoryPolicys(String carBrandId);

}
