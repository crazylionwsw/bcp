package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.api.bd.bean.PaymentPolicyBean;
import com.fuze.bcp.cardealer.domain.CarDealer;
import com.fuze.bcp.bd.domain.SourceRate;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Cardealer;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/18.
 */
public class CardealerImpl implements Cardealer {

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

        CarDealer carDealer = (CarDealer) entity;
        String address = sourceMap.getString("address");
        Double sourceDeals = sourceMap.getDouble("sourceDeals");
        String businessmanId = sourceMap.getString("businessmanId");
        List businessmanIds = new ArrayList();
        if (businessmanId != null) {
            businessmanIds.add(businessmanId);
        }
        carDealer.setBusinessManIds(businessmanIds);
        //集合
        List carBrandIds = sourceMap.get("carBrandIds") != null ? (List) sourceMap.get("carBrandIds") : null;
        if (carBrandIds != null && carBrandIds.size() == 1) {
            carDealer.setBrandIsLimit(1);
        }
        List<BasicDBObject> payAccounts = sourceMap.get("payAccounts") != null ? (List) sourceMap.get("payAccounts") : null;
        List provinceIds = sourceMap.get("provinceIds") != null ? (List) sourceMap.get("provinceIds") : null;
        String telephone = sourceMap.getString("telephone");
        String cashSourceId = sourceMap.getString("cashSourceId");
        String cell = sourceMap.getString("cell");
        String compensatoryRatio = sourceMap.getString("compensatoryRatio");
        String cooperationCashSourceId = sourceMap.getString("cooperationCashSourceId");
        String customerNumber = sourceMap.getString("customerNumber");
        Integer dataStatus = sourceMap.getInt("dataStatus");
        String employeeId = sourceMap.getString("employeeId");
        Double level = sourceMap.getDouble("level");
        String loginUserId = sourceMap.getString("loginUserId");
        String manager = sourceMap.getString("manager");
        String name = sourceMap.getString("name");
        String orginfoId = sourceMap.getString("orginfoId");
        Double payPeriod = sourceMap.getDouble("payPeriod");
        String startDate = sourceMap.getString("startDate");
        Integer status = sourceMap.getInt("status");
        String sumLoan = sourceMap.getString("sumLoan");
        String ts = sourceMap.getString("ts");
        String turnover = sourceMap.getString("turnover");
        //根据渠道经理判断所在部门  赋值经营范围
        Query queryOrg = new Query();
        queryOrg.addCriteria(Criteria.where("_id").is(new ObjectId(orginfoId)));

        com.mongodb.DBRef businessTypeDBRef = (com.mongodb.DBRef) sourceMap.get("businessType");
        BasicDBObject orgInfo = source.findOne(queryOrg, BasicDBObject.class, "bd_orginfo");

        if(businessTypeDBRef != null){
            BasicDBObject businessType = source.findOne(new Query(Criteria.where("_id").is(new ObjectId(businessTypeDBRef.getId().toString()))), BasicDBObject.class, businessTypeDBRef.getCollectionName());
            List typesList = new ArrayList<>();
            typesList.add(businessType.getString("code"));
            carDealer.setBusinessTypeCodes(typesList);
        } else if (orgInfo.getString("name").equals("新车金融部") || orgInfo.getString("name").equals("亚市金融部")) {
            List<String> bus = new ArrayList<String>();
            bus.add("NC");
            carDealer.setBusinessTypeCodes(bus);
        } else if (orgInfo.getString("name").equals("二手车金融部")) {
            List<String> bus1 = new ArrayList<String>();
            bus1.add("OC");
            carDealer.setBusinessTypeCodes(bus1);
        } else if (orgInfo != null && orgInfo.getString("parentId") != null) {
            String parentId = orgInfo.getString("parentId");
            Query queryPa = new Query();
            queryPa.addCriteria(Criteria.where("_id").is(new ObjectId(parentId)));
            BasicDBObject orgInfo1 = source.findOne(queryPa, BasicDBObject.class, "bd_orginfo");
            if (orgInfo1 != null) {
                if (orgInfo1.getString("name").equals("新车金融部") || orgInfo1.getString("name").equals("亚市金融部")) {
                    List<String> bus = new ArrayList<String>();
                    bus.add("NC");
                    carDealer.setBusinessTypeCodes(bus);
                } else if (orgInfo1.getString("name").equals("二手车金融部")) {
                    List<String> bus1 = new ArrayList<String>();
                    bus1.add("OC");
                    carDealer.setBusinessTypeCodes(bus1);
                }

            }
        }
        if (carDealer.getBusinessTypeCodes() == null || carDealer.getBusinessTypeCodes().size() == 0) {
            List list = new ArrayList<>();
            list.add("NC");
            list.add("OC");
            carDealer.setBusinessTypeCodes(list);
        }

        List<SalesRate> salesRates = new ArrayList<>();
        List<ServiceFee> serviceFeeEntityList = new ArrayList<ServiceFee>();
        carDealer.setServiceFeeEntityList(serviceFeeEntityList);
        if (carDealer.getBusinessTypeCodes().contains("NC")) {
            // 贷款服务费率
            List<RateType> rateTypeList0 = new ArrayList<>();
            RateType rr1 = new RateType();
            rr1.setMonths(12);
            rr1.setRatio(0.0);
            RateType rr2 = new RateType();
            rr2.setMonths(18);
            rr2.setRatio(0.0);
            RateType rr3 = new RateType();
            rr3.setMonths(24);
            rr3.setRatio(0.0);
            RateType rr4 = new RateType();
            rr4.setMonths(30);
            rr4.setRatio(0.0);
            RateType rr5 = new RateType();
            rr5.setMonths(36);
            rr5.setRatio(0.0);
            RateType rr6 = new RateType();
            rr6.setMonths(48);
            rr6.setRatio(0.0);
            RateType rr7 = new RateType();
            rr7.setMonths(60);
            rr7.setRatio(0.0);
            rateTypeList0.add(rr1);
            rateTypeList0.add(rr2);
            rateTypeList0.add(rr3);
            rateTypeList0.add(rr4);
            rateTypeList0.add(rr5);
            rateTypeList0.add(rr6);
            rateTypeList0.add(rr7);
            ServiceFee serviceFee = new ServiceFee();
            serviceFee.setBusinessType("NC");
            serviceFee.setRateTypeList(rateTypeList0);
            carDealer.getServiceFeeEntityList().add(serviceFee);
            // 银行手续费率
            BusinessRate businessRate = new BusinessRate();
            SourceRate sourceRate = target.findOne(new Query(Criteria.where("_id").is(new ObjectId(BusinessRate.DEF_SOURCE_RATE_ID))), SourceRate.class);
            businessRate.setSourceRateId(BusinessRate.DEF_SOURCE_RATE_ID);
            businessRate.setRateTypeList(sourceRate.getRateTypes());
            List<BusinessRate> _rateTypeList = new ArrayList<BusinessRate>();
            _rateTypeList.add(businessRate);
            SalesRate salesRate = new SalesRate();
            salesRate.setBusinessTypeCode("NC");
            salesRate.setRateTypeList(_rateTypeList);
            salesRates.add(salesRate);
            carDealer.setPaymentMethod(1);
            carDealer.setSalesPolicyId("5a1d666aba2a5d30cb614c50");
        }
        if (carDealer.getBusinessTypeCodes().contains("OC")) {
            // 贷款服务费率
            List<RateType> rateTypeList0 = new ArrayList<>();
            RateType rr1 = new RateType();
            rr1.setMonths(12);
            rr1.setRatio(0.0);
            RateType rr2 = new RateType();
            rr2.setMonths(18);
            rr2.setRatio(0.0);
            RateType rr3 = new RateType();
            rr3.setMonths(24);
            rr3.setRatio(0.0);
            RateType rr4 = new RateType();
            rr4.setMonths(30);
            rr4.setRatio(0.0);
            RateType rr5 = new RateType();
            rr5.setMonths(36);
            rr5.setRatio(0.0);
            RateType rr6 = new RateType();
            rr6.setMonths(48);
            rr6.setRatio(0.0);
            RateType rr7 = new RateType();
            rr7.setMonths(60);
            rr7.setRatio(0.0);
            rateTypeList0.add(rr1);
            rateTypeList0.add(rr2);
            rateTypeList0.add(rr3);
            rateTypeList0.add(rr4);
            rateTypeList0.add(rr5);
            rateTypeList0.add(rr6);
            rateTypeList0.add(rr7);
            ServiceFee serviceFee = new ServiceFee();
            serviceFee.setBusinessType("OC");
            serviceFee.setRateTypeList(rateTypeList0);
            carDealer.getServiceFeeEntityList().add(serviceFee);
            // 银行手续费率
            BusinessRate businessRate = new BusinessRate();
            SourceRate sourceRate = target.findOne(new Query(Criteria.where("_id").is(new ObjectId(BusinessRate.DEF_SOURCE_RATE_ID_2))), SourceRate.class);
            businessRate.setSourceRateId(BusinessRate.DEF_SOURCE_RATE_ID_2);
            businessRate.setRateTypeList(sourceRate.getRateTypes());
            List<BusinessRate> _rateTypeList = new ArrayList<BusinessRate>();
            _rateTypeList.add(businessRate);
            SalesRate salesRate1 = new SalesRate();
            salesRate1.setBusinessTypeCode("OC");
            salesRate1.setRateTypeList(_rateTypeList);
            salesRates.add(salesRate1);
            carDealer.setPaymentMethod(0);
            carDealer.setSalesPolicyId("5a1d66caba2a5d30cb614c51");
        }
        carDealer.setDealerRateTypes(salesRates);
        //provinceIds
        List provinceIdsList = new ArrayList();
        if (provinceIds != null) {
            for (String ids : (List<String>) provinceIds) {
                provinceIdsList.add(ids);
            }
        }
        carDealer.setProvinceIds(provinceIdsList);
        carDealer.setDealerRegion("北京市");
        //carBrandIds
        List carBrandIdsList = new ArrayList();
        if (carBrandIds != null) {
            for (String ids : (List<String>) carBrandIds) {
                carBrandIdsList.add(ids);
            }
        }
        carDealer.setCarBrandIds(carBrandIdsList);
        //payAccounts
        List<PayAccount> payList = new ArrayList<PayAccount>();
        boolean flag = true;
        if (payAccounts != null) {
            for (BasicDBObject obj : payAccounts) {
                PayAccount paya = new PayAccount();
                if (flag) {
                    paya.setDefaultAccount(PayAccount.ACCOUTCHECK_ALL);
                    flag = false;
                } else {
                    paya.setDefaultAccount(PayAccount.ACCOUTCHECK_NALL);
                }
                paya.setAccountNumber(obj.getString("accountNumber"));
                paya.setAccountName(obj.getString("accountName"));
                paya.setAccountType(obj.getInt("accountType"));
                paya.setBankName(obj.getString("bankName"));
                if (obj.get("defaultAccount") != null) {
                    if (obj.get("defaultAccount").equals(false)) {
                        paya.setDefaultAccount(1);
                    } else {
                        paya.setDefaultAccount(0);
                    }
                }
                paya.setAccountWay(obj.getInt("accountWay"));
                payList.add(paya);
            }
        }
        carDealer.setPayAccounts(payList);
        carDealer.setTelephone(telephone);
        carDealer.setId(sourceMap.getString("_id"));
        carDealer.setApproveStatus(2);
        carDealer.setAddress(address);
        carDealer.setSourceDeals(sourceDeals.intValue());
        if (!"58ad2da2e4b000431c11e925".equals(cashSourceId) && !"5966cd30682b623c68101beb".equals(cashSourceId)) {
            cashSourceId = "58ad2da2e4b000431c11e925";
        }
        carDealer.setCashSourceId(cashSourceId);
        carDealer.setCell(cell);
        carDealer.setCompensatoryRatio(compensatoryRatio);
        carDealer.setCooperationCashSourceId(cooperationCashSourceId);
        carDealer.setCustomerNumber(customerNumber);
        carDealer.setDataStatus(dataStatus);
        carDealer.setEmployeeId(employeeId);
        carDealer.setLevel(level.intValue());
        carDealer.setLoginUserId(loginUserId);
        carDealer.setManager(manager);
        carDealer.setName(name);
        carDealer.setOrginfoId(orginfoId);
        carDealer.setPayPeriod(payPeriod.intValue());
        carDealer.setStartDate(startDate);
        carDealer.setStatus(status);
        carDealer.setSumLoan(sumLoan);
        carDealer.setTs(ts);
        carDealer.setTurnover(turnover);
        //新数据多出(有默认值的)

        carDealer.setPaymentNewTime("2");
        carDealer.setPaymentOldTime("2");
        PaymentPolicyBean paymentPolicyBean = new PaymentPolicyBean();
        paymentPolicyBean.setBusiness(1);
        paymentPolicyBean.setDiscount(0);
        carDealer.setPaymentPolicy(paymentPolicyBean);
        return null;
    }
}
