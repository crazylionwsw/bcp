package com.fuze.bcp.api.bd.service;

import com.fuze.bcp.api.bd.bean.*;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.APITreeLookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by admin on 2017/6/9.
 */
public interface IBaseDataBizService {

    /****************************************** 单据类型维护 ******************************************/
    /**
     * 获取单据类型所有数据
     * @return
     */
    ResultBean<List<BillTypeBean>> actGetBillTypes();

    /**
     * 通过编码获取单据
     * @param code
     * @return
     */
    ResultBean<BillTypeBean> actGetBillType(String code);

    /**
     * 获取单据类型列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<BillTypeBean>> actGetBillTypes(@NotNull @Min(0) Integer currentPage);

    ResultBean<DataPageBean<BillTypeBean>> actGetBillTypeOrderBy(Integer currentPage);

    /**
     * 获取单据类型列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupBillTypes();

    /**
     * 保存单据类型
     *
     * @param billType
     * @return
     */
    ResultBean<BillTypeBean> actSaveBillType(BillTypeBean billType);

    /**
     * 删除单据类型
     *
     * @param billTypeId
     * @return
     */
    ResultBean<BillTypeBean> actDeleteBillType(@NotNull String billTypeId);

    /****************************************** 业务类型维护 ******************************************/
    ResultBean<BusinessTypeBean> actGetBusinessType(String id);

    ResultBean<List<BusinessTypeBean>> actGetAvaliableListByCodes(List<String> codes);

    /**
     * 获取业务类型所有数据
     * @return
     */
    ResultBean<List<BusinessTypeBean>> actGetBusinessTypes();
    /**
     * 获取业务类型列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<BusinessTypeBean>> actGetBusinessTypes(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取业务类型列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupBusinessTypes();

    /**
     * 获取业务类型的利率
     * @return
     */
    ResultBean<List<BusinessTypeRateLookupBean>> actLookupBusinessRates();

    /**
     * 保存业务类型
     *
     * @param businessType
     * @return
     */
    ResultBean<BusinessTypeBean> actSaveBusinessType(BusinessTypeBean businessType);

    /**
     * 删除业务类型
     *
     * @param businessTypeId
     * @return
     */
    ResultBean<BusinessTypeBean> actDeleteBusinessType(@NotNull String businessTypeId);

    /**
     * 根据code批量获取业务类型
     * @param businessTypeCodes
     * @return
     */
    ResultBean<List<BusinessTypeBean>> actGetBusinessByCodes(List<String> businessTypeCodes);

    /****************************************** 担保方式维护 ******************************************/

    /**
     * 获取担保方式所有数据
     * @return
     */
    ResultBean<List<GuaranteeWayBean>> actGetGuaranteeWays();

    /**
     * 获取担保方式列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<GuaranteeWayBean>> actGetGuaranteeWays(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取担保方式列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupGuaranteeWays();

    /**
     * 保存担保方式
     *
     * @param guaranteeWay
     * @return
     */
    ResultBean<GuaranteeWayBean> actSaveGuaranteeWay(GuaranteeWayBean guaranteeWay);

    /**
     * 删除担保方式
     *
     * @param guaranteeWayId
     * @return
     */
    ResultBean<GuaranteeWayBean> actDeleteGuaranteeWay(@NotNull String guaranteeWayId);

    /**
     *     根据   ID     回显
     * @param guaranteeWayId
     * @return
     */
    ResultBean<GuaranteeWayBean> actGetGuaranteeWay(@NotNull String guaranteeWayId);

    /****************************************** 还款方式维护 ******************************************/
    /**
     * 获取还款方式数据
     * @return
     */
    ResultBean<List<RepaymentWayBean>> actGetRepaymentWays();
    /**
     * 获取还款方式列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<RepaymentWayBean>> actGetRepaymentWays(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取还款方式列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupRepaymentWays();

    /**
     * 保存还款方式
     *
     * @param repaymentWay
     * @return
     */
    ResultBean<RepaymentWayBean> actSaveRepaymentWay(RepaymentWayBean repaymentWay);

    /**
     * 删除还款方式
     *
     * @param repaymentWayId
     * @return
     */
    ResultBean<RepaymentWayBean> actDeleteRepaymentWay(@NotNull String repaymentWayId);

    /****************************************** 档案类型维护 ******************************************/

    /**
     * 获取档案类型所有数据
     * @return
     */
    ResultBean<List<CustomerImageTypeBean>> actGetCustomerImageTypes();
    ResultBean<List<CustomerImageTypeBean>> actGetCustomerImageTypes(String bizCode, String billTypeCode);

    /**
     * 获取档案类型列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<CustomerImageTypeBean>> actGetCustomerImageTypes(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取档案类型列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupCustomerImageTypes();

    /**
     * 保存档案类型
     *
     * @param customerImageType
     * @return
     */
    ResultBean<CustomerImageTypeBean> actSaveCustomerImageType(CustomerImageTypeBean customerImageType);

    /**
     * 删除档案类型
     *
     * @param customerImageTypeId
     * @return
     */
    ResultBean<CustomerImageTypeBean> actDeleteCustomerImageType(@NotNull String customerImageTypeId);

    /**
     * 通过编码获取档案类型
     * @param code
     * @return
     */
    ResultBean<CustomerImageTypeBean> actGetCustomerImageType(@NotNull String code);



    /**
     * 根据CODE获取档案类型列表
     *
     * @param billTypeCode
     * @param bizCode  新车旧车
     * @return
     */
    ResultBean<List<CustomerImageTypeBean>> actGetCustomerImageTypesByBillType(@NotNull String billTypeCode,@NotNull String bizCode);



    /****************************************** 收费项维护 ******************************************/
    /**
     * 获取收费项列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<FeeItemBean>> actGetFeeItems(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取收费项列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupFeeItems();

    /**
     * 保存档案类型
     *
     * @param feeItem
     * @return
     */
    ResultBean<FeeItemBean> actSaveFeeItem(FeeItemBean feeItem);

    /**
     * 删除档案类型
     *
     * @param feeItemId
     * @return
     */
    ResultBean<FeeItemBean> actDeleteFeeItem(@NotNull String feeItemId);

    /**
     *      根据  ID      回显
     * @param feeItemId
     * @return
     */
    ResultBean<FeeItemBean> actGetFeeItem(@NotNull String feeItemId);

    /**
     获取所有收费项
     */
    ResultBean<List<FeeItemBean>> actGetFeeItems();

    /****************************************** 地区维护 ******************************************/
    /**
     * 获取地区列表（带分页，返回所有数据）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<ProvinceBean>> actGetProvinces(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取地区列表（不带分页）
     *
     * @return
     */
    ResultBean<List<ProvinceBean>> actGetProvinces();

    /**
     * 获取下级地区列表（parentId为0时表示获取顶级地区列表
     *
     * @param parentId
     * @return
     */
    ResultBean<List<ProvinceBean>> actGetChildProvinces(@NotNull String parentId);

    /**
     * 获取地区列表，（只返回可用数据）
     *
     * @return
     */
    ResultBean<List<APITreeLookupBean>> actLookupProvinces();

    /**
     * 保存地区
     *
     * @param province
     * @return
     */
    ResultBean<ProvinceBean> actSaveProvince(ProvinceBean province);

    /**
     * 删除地区
     *
     * @param provinceId
     * @return
     */
    ResultBean<ProvinceBean> actDeleteProvince(@NotNull String provinceId);

    /*******************************************BusinessEventTypeBean**********************************************/

    ResultBean<BusinessEventTypeBean> actGetBusinessEventType(String id);

    ResultBean<List<BusinessEventTypeBean>> actLookupBusinessEventTypes();

    ResultBean<DataPageBean<BusinessEventTypeBean>> actGetBusinessEventTypes(Integer currentPage);

    ResultBean<BusinessEventTypeBean> actSaveBusinessEventType(BusinessEventTypeBean businessEventTypeBean);

    ResultBean<BusinessEventTypeBean> actDeleteBusinessEventType(String id);

    ResultBean<BusinessEventTypeBean> actGetOneBusinessEventType(String businessType);

    ResultBean<DealerEmployeeBean> actSaveDealerEmployee(DealerEmployeeBean dealerEmployeeBean);

    ResultBean<List<DealerEmployeeBean>> actSaveDealerEmployee(List<DealerEmployeeBean> dealerEmployeeBeans);

    ResultBean<DealerEmployeeBean> actGetOneDealerEmployeeById(String id);

    ResultBean<DealerEmployeeBean> actGetAvaliableDealerEmployee();

    ResultBean<Integer> actCountDealerEmployees (String carDealerId);

    ResultBean<DealerEmployeeBean> actGetOneDealerEmployee(String id);

    ResultBean<DealerEmployeeBean> actDeleteDealerEmployee(String id);

    ResultBean<List<APILookupBean>> actLookupDealerEmployees();

    ResultBean<DataPageBean<DealerEmployeeBean>> actGetDealerEmployees(Integer currentPage, String carDealerId);

    ResultBean<List<DealerEmployeeBean>> actGetAllDealerEmployees(String carDealerId);

    ResultBean<DataPageBean<DealerEmployeeBean>> actGetDealerEmployees(Integer currentPage);
}
