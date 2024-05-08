package com.fuze.bcp.bd.business;

import com.fuze.bcp.api.bd.bean.*;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bd.domain.*;
import com.fuze.bcp.bd.service.*;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by admin on 2017/6/9.
 */
@Service
public class BizBaseDataService implements IBaseDataBizService {

    @Autowired
    MessageService messageService;

    @Autowired
    MappingService mappingService;

    @Autowired
    IBillTypeService iBillTypeService;

    @Autowired
    IBusinessTypeService iBusinessTypeService;

    @Autowired
    IGuaranteeWayService iGuaranteeWayService;

    @Autowired
    IRepaymentWayService iRepaymentWayService;

    @Autowired
    ICustomerImageTypeService iCustomerImageTypeService;

    @Autowired
    IFeeItemService iFeeItemService;

    @Autowired
    IProvinceService iProvinceService;

    @Autowired
    ICompensatoryPolicyService iCompensatoryPolicyService;

    @Autowired
    ISalesPolicyService iSalesPolicyService;

    @Autowired
    ISourceRateService iSourceRateService;

    @Autowired
    IBusinessEventTypeService iBusinessEventTypeService;

    @Autowired
    IParamBizService iParamBizService;

    @Override
    public ResultBean<List<BillTypeBean>> actGetBillTypes() {
        List<BillType> billTypes = iBillTypeService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(billTypes, BillTypeBean.class));
    }

    @Override
    public ResultBean<BillTypeBean> actGetBillType(String code) {
        BillType billType = iBillTypeService.getOneByCode(code);
        if (billType == null) {
            return ResultBean.getSucceed();
        } else {
            return ResultBean.getSucceed().setD(mappingService.map(billType, BillTypeBean.class));
        }
    }

    @Override
    public ResultBean<DataPageBean<BillTypeBean>> actGetBillTypes(Integer currentPage) {
        Page<BillType> billTypes = iBillTypeService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(billTypes, BillTypeBean.class));
    }

    @Override
    public ResultBean<DataPageBean<BillTypeBean>> actGetBillTypeOrderBy(Integer currentPage) {
        Page<BillType> billTypes = iBillTypeService.getAllOrderBy(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(billTypes, BillTypeBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupBillTypes() {

        List<BillType> billTypes = iBillTypeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(billTypes, APILookupBean.class));
    }

    @Override
    public ResultBean<BillTypeBean> actSaveBillType(BillTypeBean billType) {

        BillType savedBillType = iBillTypeService.save(mappingService.map(billType, BillType.class));
        return ResultBean.getSucceed().setD(mappingService.map(savedBillType, BillTypeBean.class));
    }

    @Override
    public ResultBean<BillTypeBean> actDeleteBillType(String billTypeId) {

        BillType billType = iBillTypeService.getOne(billTypeId);

        if (billType != null) {
            billType = iBillTypeService.delete(billTypeId);
            return ResultBean.getSucceed().setD(mappingService.map(billType, BillTypeBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<BusinessTypeBean> actGetBusinessType(String id) {
        BusinessType businessType = iBusinessTypeService.getOne(id);
        BusinessTypeBean businessTypeBean = mappingService.map(businessType, BusinessTypeBean.class);
        businessTypeBean.setFeeItems(businessType.getFeeItems());
        return ResultBean.getSucceed().setD(businessTypeBean);
    }

    @Override
    public ResultBean<List<BusinessTypeBean>> actGetBusinessTypes() {
        List<BusinessType> businessTypes = iBusinessTypeService.getAll();
        List<BusinessTypeBean> businessTypeBeans = new ArrayList<>();
        for (BusinessType businessType : businessTypes) {
            BusinessTypeBean businessTypeBean = mappingService.map(businessType, BusinessTypeBean.class);
            businessTypeBean.setFeeItems(businessType.getFeeItems());
            businessTypeBeans.add(businessTypeBean);
        }
        return ResultBean.getSucceed().setD(businessTypeBeans);
    }

    @Override
    public ResultBean<DataPageBean<BusinessTypeBean>> actGetBusinessTypes(Integer currentPage) {
        Page<BusinessType> businessTypes = iBusinessTypeService.getAll(currentPage);
        Page<BusinessTypeBean> businessTypeBeans = businessTypes.map(new Converter<BusinessType, BusinessTypeBean>() {
            @Override
            public BusinessTypeBean convert(BusinessType businessType) {
                BusinessTypeBean businessTypeBean = mappingService.map(businessType, BusinessTypeBean.class);
                businessTypeBean.setFeeItems(businessType.getFeeItems());
                return businessTypeBean;
            }
        });
        DataPageBean<BusinessTypeBean> destination = new DataPageBean<BusinessTypeBean>();
        destination.setPageSize(businessTypeBeans.getSize());
        destination.setTotalCount(businessTypeBeans.getTotalElements());
        destination.setTotalPages(businessTypeBeans.getTotalPages());
        destination.setCurrentPage(businessTypeBeans.getNumber());
        for (BusinessTypeBean t : businessTypeBeans.getContent()) {
            destination.getResult().add(t);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupBusinessTypes() {

        List<BusinessType> businessTypes = iBusinessTypeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(businessTypes, APILookupBean.class));
    }

    @Override
    public ResultBean<List<BusinessTypeRateLookupBean>> actLookupBusinessRates() {
        List<BusinessType> businessTypes = iBusinessTypeService.getAvaliableAll();
        List<BusinessTypeRateLookupBean> businessTypeRates = new ArrayList<BusinessTypeRateLookupBean>();
        List<SourceRate> sourceRates = iSourceRateService.getAvaliableAll();
        for (BusinessType bt : businessTypes) {
            BusinessTypeRateLookupBean btr = mappingService.map(bt, BusinessTypeRateLookupBean.class);

            List<SourceRateBean> sourceRateBeans = new ArrayList<SourceRateBean>();
            Map<String, List<RateType>> btRates = new HashMap<String, List<RateType>>();
            if (btr.getRateTypes() != null) {
                for (BusinessRate bizRate : btr.getRateTypes()) {
                    btRates.put(bizRate.getSourceRateId(), bizRate.getRateTypeList());
                }

                for (SourceRate sourceRate : sourceRates) {
                    if (btRates.get(sourceRate.getId()) != null) {
                        sourceRateBeans.add(mappingService.map(sourceRate, SourceRateBean.class));
                    }
                }
            }
            btr.setSourceRates(sourceRateBeans);

            businessTypeRates.add(btr);
        }


        return ResultBean.getSucceed().setD(businessTypeRates);
    }

    @Override
    public ResultBean<BusinessTypeBean> actSaveBusinessType(BusinessTypeBean businessTypeBean) {
        BusinessType businessType = mappingService.map(businessTypeBean, BusinessType.class);
        businessType.setFeeItems(businessTypeBean.getFeeItems());
        businessType = iBusinessTypeService.save(businessType);
        return ResultBean.getSucceed().setD(mappingService.map(businessType, BusinessTypeBean.class));
    }

    @Override
    public ResultBean<BusinessTypeBean> actDeleteBusinessType(String businessTypeId) {

        BusinessType businessType = iBusinessTypeService.getOne(businessTypeId);

        if (businessType != null) {
            businessType = iBusinessTypeService.delete(businessTypeId);
            return ResultBean.getSucceed().setD(mappingService.map(businessType, BusinessTypeBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<BusinessTypeBean>> actGetAvaliableListByCodes(List<String> businessTypeCodes) {
        List business = iBusinessTypeService.getAvaliableListByCodes(businessTypeCodes);
        return ResultBean.getSucceed().setD(mappingService.map(business, BusinessTypeBean.class));
    }

    @Override
    public ResultBean<List<BusinessTypeBean>> actGetBusinessByCodes(List<String> businessTypeCodes) {
        List business = iBusinessTypeService.getBusinessByCodes(businessTypeCodes);
        return ResultBean.getSucceed().setD(mappingService.map(business, BusinessTypeBean.class));
    }

    @Override
    public ResultBean<List<GuaranteeWayBean>> actGetGuaranteeWays() {
        List<GuaranteeWay> guaranteeWays = iGuaranteeWayService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(guaranteeWays, GuaranteeWayBean.class));
    }

    @Override
    public ResultBean<DataPageBean<GuaranteeWayBean>> actGetGuaranteeWays(Integer currentPage) {

        Page<GuaranteeWay> guaranteeWays = iGuaranteeWayService.getAllOrderByAsc(currentPage);

        return ResultBean.getSucceed().setD(mappingService.map(guaranteeWays, GuaranteeWayBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupGuaranteeWays() {

        List<GuaranteeWay> guaranteeWays = iGuaranteeWayService.getAvaliableAll();

        return ResultBean.getSucceed().setD(mappingService.map(guaranteeWays, APILookupBean.class));
    }

    @Override
    public ResultBean<GuaranteeWayBean> actSaveGuaranteeWay(GuaranteeWayBean guaranteeWay) {

        GuaranteeWay guaranteeWay1 = iGuaranteeWayService.save(mappingService.map(guaranteeWay, GuaranteeWay.class));

        return ResultBean.getSucceed().setD(mappingService.map(guaranteeWay1, GuaranteeWayBean.class));
    }

    @Override
    public ResultBean<GuaranteeWayBean> actDeleteGuaranteeWay(String guaranteeWayId) {

        GuaranteeWay guaranteeWay = iGuaranteeWayService.getOne(guaranteeWayId);

        if (guaranteeWay != null) {
            guaranteeWay = iGuaranteeWayService.delete(guaranteeWayId);
            return ResultBean.getSucceed().setD(mappingService.map(guaranteeWay, GuaranteeWayBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<GuaranteeWayBean> actGetGuaranteeWay(@NotNull String guaranteeWayId) {
        GuaranteeWay guaranteeWay = iGuaranteeWayService.getOne(guaranteeWayId);

        if (guaranteeWay != null) {
            return ResultBean.getSucceed().setD(mappingService.map(guaranteeWay, GuaranteeWayBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<RepaymentWayBean>> actGetRepaymentWays() {
        List<RepaymentWay> repaymentWays = iRepaymentWayService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(repaymentWays, RepaymentWayBean.class));
    }


    @Override
    public ResultBean<DataPageBean<RepaymentWayBean>> actGetRepaymentWays(Integer currentPage) {

        Page<RepaymentWay> repaymentWays = iRepaymentWayService.getAllOrderByAsc(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(repaymentWays, RepaymentWayBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupRepaymentWays() {

        List<RepaymentWay> repaymentWays = iRepaymentWayService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(repaymentWays, APILookupBean.class));
    }

    @Override
    public ResultBean<RepaymentWayBean> actSaveRepaymentWay(RepaymentWayBean repaymentWay) {

        RepaymentWay repaymentWay1 = iRepaymentWayService.save(mappingService.map(repaymentWay, RepaymentWay.class));
        return ResultBean.getSucceed().setD(mappingService.map(repaymentWay1, RepaymentWayBean.class));
    }

    @Override
    public ResultBean<RepaymentWayBean> actDeleteRepaymentWay(String repaymentWayId) {

        RepaymentWay repaymentWay = iRepaymentWayService.getOne(repaymentWayId);

        if (repaymentWay != null) {
            repaymentWay = iRepaymentWayService.delete(repaymentWayId);
            return ResultBean.getSucceed().setD(mappingService.map(repaymentWay, RepaymentWayBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<CustomerImageTypeBean>> actGetCustomerImageTypes() {
        List<CustomerImageType> customerImageTypes = iCustomerImageTypeService.getAllOrderByCode();
        return ResultBean.getSucceed().setD(mappingService.map(customerImageTypes, CustomerImageTypeBean.class));
    }

    @Override
    public ResultBean<DataPageBean<CustomerImageTypeBean>> actGetCustomerImageTypes(Integer currentPage) {

        Page<CustomerImageType> customerImageTypes = iCustomerImageTypeService.getAll(currentPage);

        return ResultBean.getSucceed().setD(mappingService.map(customerImageTypes, CustomerImageTypeBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupCustomerImageTypes() {

        List<CustomerImageType> customerImageTypes = iCustomerImageTypeService.getAvaliableAll();

        return ResultBean.getSucceed().setD(mappingService.map(customerImageTypes, APILookupBean.class));
    }

    @Override
    public ResultBean<CustomerImageTypeBean> actSaveCustomerImageType(CustomerImageTypeBean customerImageType) {

        CustomerImageType customerImageType1 = iCustomerImageTypeService.save(mappingService.map(customerImageType, CustomerImageType.class));

        return ResultBean.getSucceed().setD(mappingService.map(customerImageType1, CustomerImageTypeBean.class));
    }

    @Override
    public ResultBean<CustomerImageTypeBean> actDeleteCustomerImageType(String customerImageTypeId) {

        CustomerImageType customerImageType = iCustomerImageTypeService.getOne(customerImageTypeId);

        if (customerImageType != null) {
            customerImageType = iCustomerImageTypeService.delete(customerImageTypeId);
            return ResultBean.getSucceed().setD(mappingService.map(customerImageType, CustomerImageTypeBean.class));
        }

        return ResultBean.getFailed();

    }

    @Override
    public ResultBean<CustomerImageTypeBean> actGetCustomerImageType(@NotNull String code) {
        CustomerImageType cit = iCustomerImageTypeService.getOneByCode(code);
        if (cit == null)
            return ResultBean.getSucceed();

        return ResultBean.getSucceed().setD(mappingService.map(cit, CustomerImageTypeBean.class));
    }

    @Override
    public ResultBean<List<CustomerImageTypeBean>> actGetCustomerImageTypesByBillType(String code,String bizCode) {
        Map<?, ?> padImages = iParamBizService.actGetMap("PAD_IMAGES_ORDER").getD();
        Map<?, ?> padBizImages = (Map<?, ?>) padImages.get(bizCode);
        List<String> imageTypeCodeList = (List<String>)padBizImages.get(code);

        if (imageTypeCodeList == null) {
            return ResultBean.getFailed().setM("类型CODE错误，请确认后重试！");
        }
        List<CustomerImageType> list = iCustomerImageTypeService.findCustomerImageTypesByCodes(imageTypeCodeList);
        if (list == null) {
            return ResultBean.getFailed().setM("文件列表为空，请联系管理员设置！");
        }
        return ResultBean.getSucceed().setD(mappingService.map(list, CustomerImageTypeBean.class));
    }

    @Override
    public ResultBean<List<CustomerImageTypeBean>> actGetCustomerImageTypes(String bizCode, String billTypeCode) {
        BillType billType = iBillTypeService.getOneByCode(billTypeCode);
        if (billType == null) {
            return ResultBean.getFailed().setM("类型CODE错误，请确认后重试！");
        }
        BusinessType bizType = iBusinessTypeService.getOneByCode(bizCode);

        //获取业务所需的档案类型和单据所需的档案类型
        List<String> billCustomerImageTypeCodes = billType.getRequiredImageTypeCodes();
        List<String> bizCustomerImageTypeCodes = bizType.getCustomerImageTypeCodes();

        //求交集
        List list = new ArrayList(Arrays.asList(new Object[billCustomerImageTypeCodes.size()]));
        Collections.copy(list, billCustomerImageTypeCodes);
        list.retainAll(bizCustomerImageTypeCodes);

        List<CustomerImageType> customerImageTypes = iCustomerImageTypeService.findCustomerImageTypesByCodes(list);
        if (customerImageTypes == null || customerImageTypes.size() == 0) {
            return ResultBean.getFailed().setM("文件列表为空，请联系管理员设置！");
        }

        List<CustomerImageType> result = new ArrayList<CustomerImageType>();
        for (String itCode : billCustomerImageTypeCodes) {
            CustomerImageType customerImageType = this.getCustomerImageTypeByCode(itCode, customerImageTypes);
            if (customerImageType != null)
                result.add(customerImageType);
        }

        return ResultBean.getSucceed().setD(mappingService.map(result, CustomerImageTypeBean.class));
    }

    private CustomerImageType getCustomerImageTypeByCode(String code, List<CustomerImageType> customerImageTypes) {
        for (CustomerImageType cit : customerImageTypes) {
            if (cit.getCode().equals(code)) {
                return cit;
            }
        }

        return null;
    }

    @Override
    public ResultBean<DataPageBean<FeeItemBean>> actGetFeeItems(Integer currentPage) {
        Page<FeeItem> feeItems = iFeeItemService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(feeItems, FeeItemBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupFeeItems() {
        List<FeeItem> feeItems = iFeeItemService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(feeItems, APILookupBean.class));
    }

    @Override
    public ResultBean<FeeItemBean> actSaveFeeItem(FeeItemBean feeItem) {
        FeeItem feeItem1 = iFeeItemService.save(mappingService.map(feeItem, FeeItem.class));
        return ResultBean.getSucceed().setD(mappingService.map(feeItem1, FeeItemBean.class));
    }

    @Override
    public ResultBean<FeeItemBean> actDeleteFeeItem(String feeItemId) {
        FeeItem feeItem = iFeeItemService.getOne(feeItemId);

        if (feeItem != null) {
            feeItem = iFeeItemService.delete(feeItemId);
            return ResultBean.getSucceed().setD(mappingService.map(feeItem, FeeItemBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<FeeItemBean> actGetFeeItem(@NotNull String feeItemId) {
        FeeItem feeItem = iFeeItemService.getOne(feeItemId);
        if (feeItem != null) {
            return ResultBean.getSucceed().setD(mappingService.map(feeItem, FeeItemBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<FeeItemBean>> actGetFeeItems() {
        List<FeeItem> feeItems = iFeeItemService.getAll();
        if(feeItems != null){
            return ResultBean.getSucceed().setD(mappingService.map(feeItems,FeeItemBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<ProvinceBean>> actGetProvinces(Integer currentPage) {
        Page<Province> provinces = iProvinceService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(provinces, ProvinceBean.class));
    }

    @Override
    public ResultBean<List<ProvinceBean>> actGetProvinces() {
        List<Province> provinces = iProvinceService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(provinces, ProvinceBean.class));
    }

    @Override
    public ResultBean<List<ProvinceBean>> actGetChildProvinces(String parentId) {

        List<Province> provinces = iProvinceService.getChildren(parentId);

        return ResultBean.getSucceed().setD(mappingService.map(provinces, ProvinceBean.class));
    }

    @Override
    public ResultBean<List<APITreeLookupBean>> actLookupProvinces() {
        List<Province> provinces = iProvinceService.getLookups(null, 0);
        return ResultBean.getSucceed().setD(mappingService.map(provinces, APITreeLookupBean.class));
    }

    @Override
    public ResultBean<ProvinceBean> actSaveProvince(ProvinceBean province) {
        Province province1 = iProvinceService.save(mappingService.map(province, Province.class));
        return ResultBean.getSucceed().setD(mappingService.map(province1, ProvinceBean.class));
    }

    @Override
    public ResultBean<ProvinceBean> actDeleteProvince(String provinceId) {
        Province province = iProvinceService.getOne(provinceId);
        if (province != null) {
            province = iProvinceService.delete(provinceId);
            return ResultBean.getSucceed().setD(mappingService.map(province, ProvinceBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<BusinessEventTypeBean> actGetBusinessEventType(String id) {
        BusinessEventType businessEventType = iBusinessEventTypeService.getAvailableOne(id);
        if (businessEventType != null) {
            return ResultBean.getSucceed().setD(mappingService.map(businessEventType, BusinessEventTypeBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<BusinessEventTypeBean>> actLookupBusinessEventTypes() {
        List<BusinessEventType> businessEventTypes = iBusinessEventTypeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(businessEventTypes, BusinessEventTypeBean.class));
    }

    @Override
    public ResultBean<DataPageBean<BusinessEventTypeBean>> actGetBusinessEventTypes(Integer currentPage) {
        Page<BusinessEventType> businessEventTypes = iBusinessEventTypeService.getAllOrderByEventTypeCode(currentPage);

        return ResultBean.getSucceed().setD(mappingService.map(businessEventTypes, BusinessEventTypeBean.class));
    }

    @Override
    public ResultBean<BusinessEventTypeBean> actSaveBusinessEventType(BusinessEventTypeBean businessEventTypeBean) {
        BusinessEventType businessEventType = mappingService.map(businessEventTypeBean, BusinessEventType.class);
        businessEventType = iBusinessEventTypeService.save(businessEventType);
        return ResultBean.getSucceed().setD(mappingService.map(businessEventType, BusinessEventTypeBean.class));
    }

    @Override
    public ResultBean<BusinessEventTypeBean> actDeleteBusinessEventType(String id) {
        BusinessEventType businessEventType = iBusinessEventTypeService.delete(id);
        return ResultBean.getSucceed().setD(mappingService.map(businessEventType, BusinessEventTypeBean.class));
    }

    @Override
    public ResultBean<BusinessEventTypeBean> actGetOneBusinessEventType(String businessType) {
        BusinessEventType businessEventType = iBusinessEventTypeService.getOneByBusinessType(businessType);
        if (businessEventType != null) {
            return ResultBean.getSucceed().setD(mappingService.map(businessEventType, BusinessEventTypeBean.class));
        }
        return ResultBean.getFailed();
    }

    @Autowired
    IDealerEmployeeService iDealerEmployeeService;

    public ResultBean<DealerEmployeeBean> actSaveDealerEmployee(DealerEmployeeBean dealerEmployeeBean){
        DealerEmployee d = mappingService.map(dealerEmployeeBean, DealerEmployee.class);
        iDealerEmployeeService.save(d);
        return ResultBean.getSucceed().setD(mappingService.map(d, DealerEmployeeBean.class));
    }

    public ResultBean<List<DealerEmployeeBean>> actSaveDealerEmployee(List<DealerEmployeeBean> list){
        List<DealerEmployee> d = mappingService.map(list, DealerEmployee.class);
        iDealerEmployeeService.save(d);
        return ResultBean.getSucceed().setD(mappingService.map(d, DealerEmployeeBean.class));
    }

    @Override
    public ResultBean<DealerEmployeeBean> actGetOneDealerEmployeeById(String id) {
        DealerEmployee dealerEmployee = iDealerEmployeeService.getOne(id);
        if(dealerEmployee != null){
            return ResultBean.getSucceed().setD(mappingService.map(dealerEmployee,DealerEmployeeBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DealerEmployeeBean> actGetAvaliableDealerEmployee() {
        List<DealerEmployee> dealerEmployees = iDealerEmployeeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(dealerEmployees,DealerEmployeeBean.class));
    }

    @Override
    public ResultBean<DealerEmployeeBean> actDeleteDealerEmployee(String id) {
        DealerEmployee dealerEmployee = iDealerEmployeeService.getOne(id);
        if (dealerEmployee != null) {
            dealerEmployee = iDealerEmployeeService.delete(id);
            return ResultBean.getSucceed().setD(mappingService.map(dealerEmployee, DealerEmployeeBean.class)).setM(messageService.getMessage("MSG_DEALEREMPLOYEE_DELETE_SUCCESS"));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_DEALEREMPLOYEE_DELETE_ERROR"));
    }

    @Override
    public ResultBean<DealerEmployeeBean> actGetOneDealerEmployee(String id) {
        DealerEmployee dealerEmployee = iDealerEmployeeService.getOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(dealerEmployee, DealerEmployeeBean.class));
    }

    @Override
    public ResultBean<Integer> actCountDealerEmployees (String carDealerId) {
        return ResultBean.getSucceed().setD(iDealerEmployeeService.countDealerEmployees(carDealerId));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupDealerEmployees() {
        List<DealerEmployee> dealerEmployees = iDealerEmployeeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(dealerEmployees, APILookupBean.class));
    }

    @Override
    public ResultBean<DataPageBean<DealerEmployeeBean>> actGetDealerEmployees(Integer currentPage, String carDealerId) {
        Page<DealerEmployee> dealerEmployees = iDealerEmployeeService.getAllByDealerEmployees(carDealerId, currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(dealerEmployees, DealerEmployeeBean.class));
    }

    @Override
    public ResultBean<DataPageBean<DealerEmployeeBean>> actGetDealerEmployees(Integer currentPage) {
        Page<DealerEmployee> dealerEmployees = iDealerEmployeeService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(dealerEmployees, DealerEmployeeBean.class));
    }

    @Override
    public ResultBean<List<DealerEmployeeBean>> actGetAllDealerEmployees(String carDealerId) {
        List<DealerEmployee> employeesList = iDealerEmployeeService.getAllDealerEmployees(carDealerId);
        return ResultBean.getSucceed().setD(mappingService.map(employeesList, DealerEmployeeBean.class));
    }



}