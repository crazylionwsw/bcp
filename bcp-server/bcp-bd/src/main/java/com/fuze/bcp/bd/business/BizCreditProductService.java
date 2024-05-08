package com.fuze.bcp.bd.business;

import com.fuze.bcp.api.bd.bean.*;
import com.fuze.bcp.api.bd.service.IProductBizService;
import com.fuze.bcp.api.drools.bean.AccrualSubsidiesBean;
import com.fuze.bcp.api.drools.bean.DroolsBaseBean;
import com.fuze.bcp.api.drools.service.IDroolsBizService;
import com.fuze.bcp.bd.domain.*;
import com.fuze.bcp.bd.service.*;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by admin on 2017/6/9.
 */
@Service
public class BizCreditProductService implements IProductBizService {

    Logger logger = LoggerFactory.getLogger(BizCreditProductService.class);

    @Autowired
    ICreditProductService iCreditProductService;

    @Autowired
    ICompensatoryPolicyService iCompensatoryPolicyService;

    @Autowired
    ICompensatoryPolicyFormulaService iCompensatoryPolicyFormulaService;

    @Autowired
    ISalesPolicyService iSalesPolicyService;

    @Autowired
    IPromotionPolicyService iPromotionPolicyService;

    @Autowired
    MappingService mappingService;

    @Autowired
    IBusinessTypeService iBusinessTypeService;

    @Autowired
    ICarTypeService iCarTypeService;

    @Autowired
    IDroolsBizService iDroolsBizService;

    @Autowired
    MessageService messageService;

    @Override
    public ResultBean<DataPageBean<CreditProductBean>> actGetCreditProducts(Integer currentPage) {

        Page<CreditProduct> creditProducts = iCreditProductService.getAll(currentPage);

        return ResultBean.getSucceed().setD(mappingService.map(creditProducts, CreditProductBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupCreditProducts() {

        List<CreditProduct> creditProducts = iCreditProductService.getAvaliableAll();

        return ResultBean.getSucceed().setD(mappingService.map(creditProducts, APILookupBean.class));
    }

    @Override
    public ResultBean<CreditProductBean> actSaveCreditProduct(CreditProductBean creditProduct) {

        CreditProduct creditProduct1 = iCreditProductService.save(mappingService.map(creditProduct, CreditProduct.class));

        return ResultBean.getSucceed().setD(mappingService.map(creditProduct1, CreditProductBean.class));
    }

    @Override
    public ResultBean<CreditProductBean> actDeleteCreditProduct(String creditProductId) {
        CreditProduct creditProduct1 = iCreditProductService.getOne(creditProductId);
        if (creditProduct1 != null) {
            creditProduct1 = iCreditProductService.delete(creditProductId);
            return ResultBean.getSucceed().setD(mappingService.map(creditProduct1, CreditProductBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<CreditProductBean>> actGetCreditProducts() {
        List<CreditProduct> creditProducts = iCreditProductService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(creditProducts, CreditProductBean.class));
    }

    @Override
    public ResultBean<CreditProductBean> actGetCreditProduct(String creditProductId) {
        return ResultBean.getSucceed().setD(mappingService.map(iCreditProductService.getOne(creditProductId), CreditProductBean.class));
    }


    @Override
    public ResultBean<DataPageBean<CompensatoryPolicyBean>> actGetCompensatoryPolicies(Integer currentPage) {
        Page<CompensatoryPolicy> compensatoryPolicies = iCompensatoryPolicyService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(compensatoryPolicies, CompensatoryPolicyBean.class));
    }

    public ResultBean<DataPageBean<CompensatoryPolicyListBean>> actGetCompensatoryPolicies(Integer pageIndex, Integer pageSize) {
        Page<CompensatoryPolicy> compensatoryPolicies = iCompensatoryPolicyService.findByDate(DateTimeUtils.getCreateTime(), pageIndex, pageSize);
        return ResultBean.getSucceed().setD(mappingService.map(compensatoryPolicies, CompensatoryPolicyListBean.class));
    }

    @Override
    public ResultBean<DataPageBean<CompensatoryPolicyFormulaBean>> actGetFormulas(Integer currentPage) {
        Page page = iCompensatoryPolicyFormulaService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(page, CompensatoryPolicyFormulaBean.class));
    }

    @Override
    public ResultBean<List<CompensatoryPolicyFormulaBean>> actGetFormulas() {
        List list = iCompensatoryPolicyFormulaService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(list, CompensatoryPolicyFormulaBean.class));
    }

    @Override
    public ResultBean<CompensatoryPolicyFormulaBean> actSaveFormula(CompensatoryPolicyFormulaBean compensatoryPolicyFormulaBean) {
        CompensatoryPolicyFormula compensatoryPolicyFormula = iCompensatoryPolicyFormulaService.save(mappingService.map(compensatoryPolicyFormulaBean, CompensatoryPolicyFormula.class));
        return ResultBean.getSucceed().setD(mappingService.map(compensatoryPolicyFormula, CompensatoryPolicyFormulaBean.class));
    }

    @Override
    public ResultBean<CompensatoryPolicyFormulaBean> actDeleteFormula(String formulaId) {
        CompensatoryPolicyFormula compensatoryPolicyFormula = iCompensatoryPolicyFormulaService.delete(formulaId);
        return ResultBean.getSucceed().setD(mappingService.map(compensatoryPolicyFormula, CompensatoryPolicyFormulaBean.class));
    }

    @Override
    public ResultBean<CompensatoryPolicyFormulaBean> actGetFormula(String id) {
        CompensatoryPolicyFormula compensatoryPolicyFormula = iCompensatoryPolicyFormulaService.getAvailableOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(compensatoryPolicyFormula, CompensatoryPolicyFormulaBean.class));
    }

    @Override
    public ResultBean<CompensatoryPolicyBean> actSaveCompensatoryPolicy(CompensatoryPolicyBean compensatoryPolicy) {
        CompensatoryPolicy compensatorypolicy1 = iCompensatoryPolicyService.save(mappingService.map(compensatoryPolicy, CompensatoryPolicy.class));
        return ResultBean.getSucceed().setD(mappingService.map(compensatorypolicy1, CompensatoryPolicyBean.class));
    }

    @Override
    public ResultBean<CompensatoryPolicyBean> actDeleteCompensatoryPolicy(String compensatorypolicyId) {
        CompensatoryPolicy compensatoryPolicy = iCompensatoryPolicyService.getOne(compensatorypolicyId);
        if (compensatoryPolicy != null) {
            compensatoryPolicy = iCompensatoryPolicyService.delete(compensatorypolicyId);
            return ResultBean.getSucceed().setD(mappingService.map(compensatoryPolicy, CompensatoryPolicyBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<APILookupBean> actLookupCompensatoryPolicies() {
        List<CompensatoryPolicy> compensatoryPolicies = iCompensatoryPolicyService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(compensatoryPolicies, CompensatoryPolicyBean.class));
    }

    @Override
    public ResultBean<CompensatoryPolicyBean> actGetCompensatoryPolicy(String compensatorypolicyId) {
        return ResultBean.getSucceed().setD(mappingService.map(iCompensatoryPolicyService.getOne(compensatorypolicyId), CompensatoryPolicyBean.class));
    }

    /**
     * 根据厂商和当前日期获取贴息政策
     *
     * @param carBrandId
     * @param date
     * @return
     */
    public ResultBean<List<CompensatoryPolicyBean>> actGetCompensatoryPolicy(String carBrandId, String date) {
        List<CompensatoryPolicy> compensatoryPolicy = iCompensatoryPolicyService.findByCarBrandAndDate(carBrandId, date);
        if (compensatoryPolicy == null || compensatoryPolicy.size() == 0)
            return ResultBean.getSucceed().setD(null);

        return ResultBean.getSucceed().setD(mappingService.map(compensatoryPolicy, CompensatoryPolicyBean.class));
    }

    /**
     * 分页获取厂商和当前日期贴息政策
     *
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultBean<DataPageBean<CompensatoryPolicyListBean>> actGetCompensatoryPolicyPage(String date, Integer pageIndex, Integer pageSize) {
        Page<CompensatoryPolicy> page = iCompensatoryPolicyService.findByDate(date, pageIndex, pageSize);
        if (page == null || page.getContent().size() == 0) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_COMPENSATORY_POLICY_NULL"));
        }
        Page<CompensatoryPolicyListBean> beans = page.map(new Converter<CompensatoryPolicy, CompensatoryPolicyListBean>() {
            @Override
            public CompensatoryPolicyListBean convert(CompensatoryPolicy compensatoryPolicy) {
                return mappingService.map(compensatoryPolicy, CompensatoryPolicyListBean.class);
            }
        });
        DataPageBean<CompensatoryPolicyListBean> dataPageBean = new DataPageBean<CompensatoryPolicyListBean>();
        dataPageBean.setResult(beans.getContent());
        dataPageBean.setTotalPages(page.getTotalPages());
        dataPageBean.setTotalCount(page.getTotalElements());
        dataPageBean.setPageSize(page.getSize());
        dataPageBean.setCurrentPage(page.getNumber());

        return ResultBean.getSucceed().setD(dataPageBean);
    }

    @Autowired
    Configuration configuration;

    public ResultBean<AccrualSubsidiesBean> actGetCompensatory(AccrualSubsidiesBean accrual) {
        CarType carType = iCarTypeService.getAvailableOne(accrual.getCarTypeId());
        accrual.setCarBrandId(carType.getCarBrandId());
        accrual.setCarModelId(carType.getCarModelId());
        //根据厂商获取贴片政策
        String currentDate = DateTimeUtils.getCreateTime();
        List<CompensatoryPolicyBean> compensatoryPolicys = this.actGetCompensatoryPolicy(carType.getCarBrandId(), currentDate).getD();
        if (compensatoryPolicys == null || compensatoryPolicys.size() == 0)
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_COMPENSATORY_POLICY_BRAND_NULL"));

        //调用drools计算贴息额
        DroolsBaseBean droolsBaseBean = new DroolsBaseBean<AccrualSubsidiesBean>();
        droolsBaseBean.setApiBaseBean(accrual);
        Boolean exFlag = false;
        for (CompensatoryPolicyBean compensatoryPolicy : compensatoryPolicys) {
            String interfacePath = "com.fuze.bcp.drools";
            try {
                String templateId = compensatoryPolicy.getTemplateId();
                CompensatoryPolicyFormula compensatoryPolicyFormula = iCompensatoryPolicyFormulaService.getAvailableOne(templateId);
                if (compensatoryPolicyFormula == null) {
                    throw new Exception("未找到对应贴息贴息模板，templateId：" + templateId);
                }
                StringTemplateLoader st = new StringTemplateLoader();
                configuration.setTemplateLoader(st);
                st.putTemplate(compensatoryPolicyFormula.getCode(), compensatoryPolicyFormula.getFormulaTemplate());
                Template temp = configuration.getTemplate(compensatoryPolicyFormula.getCode());
                StringWriter writer = new StringWriter();
                Map templateMap = compensatoryPolicy.getTemplateMap() != null?compensatoryPolicy.getTemplateMap():new HashMap<>();
                temp.process(templateMap, writer);
                String content = writer.toString();
                accrual = (AccrualSubsidiesBean) iDroolsBizService.doCheckByRulStr(content, interfacePath, accrual);
                //返回贴息说明
                if (accrual.getCompensatoryAmount() != null && accrual.getCompensatoryAmount() > 0) { // 成功
                    accrual.setComment(compensatoryPolicy.getComment());
                    DecimalFormat df = new DecimalFormat("#0.00");
                    accrual.setCompensatoryAmount(df.parse(df.format(accrual.getCompensatoryAmount())).doubleValue());
                    return ResultBean.getSucceed().setD(accrual);
                }
            } catch (Exception ex) {
                logger.error("贴息计算失败", ex);
                exFlag = true;

            }
        }
        if (exFlag) {
            return ResultBean.getFailed().setM("贴息计算失败！");
        } else {
            return ResultBean.getFailed().setM("当前车型无贴息政策！");
        }
    }

    @Override
    public ResultBean<DataPageBean<PromotionPolicyBean>> actGetPromotionPolicies(Integer currentPage) {
        Page<PromotionPolicy> salesPolicies = iPromotionPolicyService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(salesPolicies, PromotionPolicyBean.class));
    }

    @Override
    public ResultBean<PromotionPolicyBean> actSavePromotionPolicy(PromotionPolicyBean promotionPolicy) {
        PromotionPolicy promotionPolicy1 = iPromotionPolicyService.save(mappingService.map(promotionPolicy, PromotionPolicy.class));
        return ResultBean.getSucceed().setD(mappingService.map(promotionPolicy1, PromotionPolicyBean.class));
    }

    @Override
    public ResultBean<PromotionPolicyBean> actDeletePromotionPolicy(String promotionpolicyId) {
        PromotionPolicy promotionPolicy = iPromotionPolicyService.getOne(promotionpolicyId);
        if (promotionPolicy != null) {
            promotionPolicy = iPromotionPolicyService.delete(promotionpolicyId);
            return ResultBean.getSucceed().setD(mappingService.map(promotionPolicy, PromotionPolicyBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<APILookupBean> actLookupPromotionPolicies() {
        List<PromotionPolicy> salesPolicies = iPromotionPolicyService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(salesPolicies, PromotionPolicyBean.class));
    }

    @Override
    public ResultBean<List<SalesPolicyBean>> actGetSalesPolicies() {
        List<SalesPolicy> salesPolicies = iSalesPolicyService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(salesPolicies, SalesPolicyBean.class));
    }

    @Override
    public ResultBean<SalesPolicyBean> actSaveSalesPolicy(SalesPolicyBean salesPolicy) {
        SalesPolicy salesPolicy1 = iSalesPolicyService.save(mappingService.map(salesPolicy, SalesPolicy.class));
        return ResultBean.getSucceed().setD(mappingService.map(salesPolicy1, SalesPolicyBean.class));
    }

    @Override
    public ResultBean<SalesPolicyBean> actDeleteSalesPolicy(@NotNull String salespolicyId) {
        SalesPolicy salesPolicy = iSalesPolicyService.getOne(salespolicyId);
        if (salesPolicy != null) {
            salesPolicy = iSalesPolicyService.delete(salespolicyId);
            return ResultBean.getSucceed().setD(mappingService.map(salesPolicy, SalesPolicyBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<APILookupBean> actLookupSalesPolicies() {
        List<SalesPolicy> salesPolicies = iSalesPolicyService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(salesPolicies, SalesPolicyBean.class));
    }

    @Override
    public ResultBean<List<SalesPolicyBean>> actSalesPolicys() {
        List<SalesPolicy> salesPolicies = iSalesPolicyService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(salesPolicies, SalesPolicyBean.class));
    }

    @Override
    public ResultBean<List<PadSalesPolicyBean>> actGetPadSalesPolicys() {
        List<SalesPolicy> salesPolicies = iSalesPolicyService.getAvaliableAll();
        List<PadSalesPolicyBean> padPolicyList = new ArrayList<PadSalesPolicyBean>();
        List<PadSalesRate> rateList = new ArrayList<PadSalesRate>();
        PadSalesPolicyBean padPolicy = null;
        SalesPolicy policy = null;
        for (int i = 0; salesPolicies != null && i < salesPolicies.size(); i++) {
            policy = (SalesPolicy) salesPolicies.get(i);
            if (policy != null) {
                rateList = new ArrayList<>();
                padPolicy = mappingService.map(policy, PadSalesPolicyBean.class);
                List<SalesRate> slist = salesPolicies.get(i).getSalesRates();
                Iterator<SalesRate> itr = slist.iterator();
                while (itr.hasNext()) {
                    SalesRate sale = itr.next();
                    if (sale != null) {
                        PadSalesRate psale = new PadSalesRate();
                        BusinessType businessType = iBusinessTypeService.getOneByCode(sale.getBusinessTypeCode());
                        if (businessType != null && sale.getRateTypeList().size() > 0) {
                            psale.setBusinessType(businessType.getCode());
                            psale.setBusinessTypeName(businessType.getName());
                            psale.setBusinessTypeId(businessType.getId());
                            psale.setRateTypeList(sale.getRateTypeList().get(0).getRateTypeList());
                            rateList.add(psale);
                        }
                    }
                }
                if (rateList != null) {
                    padPolicy.setSalesRates(rateList);
                    String beanStr = padPolicy.toString();
                    if (beanStr.indexOf("NC") > 0 && beanStr.indexOf("OC") > 0) {
                        padPolicy.setFlag(PadSalesPolicyBean.DEF_FLAG_ALL_CAR);
                    } else if (beanStr.indexOf("NC") > 0) {
                        padPolicy.setFlag(PadSalesPolicyBean.DEF_FLAG_NWE_CAR);
                    } else if (beanStr.indexOf("OC") > 0) {
                        padPolicy.setFlag(PadSalesPolicyBean.DEF_FLAG_OLD_CAR);
                    }
                    if (padPolicy != null) {
                        padPolicyList.add(padPolicy);
                    }
                }
            }
        }
        if (padPolicyList == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_SALES_POLICY_NULL"));
        } else {
            return ResultBean.getSucceed().setD(padPolicyList);
        }
    }

    @Override
    public ResultBean<List<CompensatoryPolicyBean>> actGetCompensatoryPolicys(String carBrandId) {
        List<CompensatoryPolicy> compensatoryPolicy = iCompensatoryPolicyService.findAllByCarBrandAndDate(carBrandId, DateTimeUtils.getCreateTime());
        if (compensatoryPolicy.isEmpty())
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_COMPENSATORY_POLICY_BRAND_NULL"));

        return ResultBean.getSucceed().setD(mappingService.map(compensatoryPolicy, CompensatoryPolicyBean.class));
    }

}
