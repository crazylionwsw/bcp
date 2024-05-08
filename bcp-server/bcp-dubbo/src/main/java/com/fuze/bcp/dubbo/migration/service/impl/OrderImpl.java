package com.fuze.bcp.dubbo.migration.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.cardealer.domain.CarDealer;
import com.fuze.bcp.bd.domain.CarType;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.RateType;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.customer.domain.CustomerCar;
import com.fuze.bcp.customer.domain.CustomerLoan;
import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.dubbo.migration.DataMigration;
import com.fuze.bcp.dubbo.migration.Main;
import com.fuze.bcp.sys.domain.SysParam;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/10/17.
 */
public class OrderImpl implements DataMigration {

    @Override
    public Map<String, String> billDataMigration(CustomerDemand customerDemand, CustomerTransaction transaction, BasicDBObject sourceObj, BaseBillEntity targetObj, MongoTemplate source, MongoTemplate target)  {
        PurchaseCarOrder order = (PurchaseCarOrder) targetObj;
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(order.getCarDealerId())));
        CarDealer carDealer = target.findOne(query, CarDealer.class);
        if (sourceObj.get("filenumber") != null) {
            transaction.setFileNumber((String)sourceObj.get("filenumber"));
        }
        order.setCounterGuarantorId((String) sourceObj.get("guaranteeCustomerId"));
        order.setApproveStatus(sourceObj.getInt("approveStatus"));
        CustomerCar customerCar = new CustomerCar();
        Map<String, String> selectCar = (Map<String, String>) sourceObj.get("selectCar");
        customerCar.setCarColorName(selectCar.containsKey("color")?selectCar.get("color"):"");
        //  从系统参配项中查询车辆颜色配置，然后根据1.0车辆颜色得到相对应的颜色编码，并保存
        SysParam sysParams = target.findOne(new Query(Criteria.where("code").is("CAR_COLORS")), SysParam.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Map<String,String>> list = null;
        try {
            list = objectMapper.readValue(sysParams.getParameterValue(), ArrayList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map<String,String> map : list){
            if (map.get("name").equals(selectCar.get("color"))){
                customerCar.setCarColor((String) map.get("code"));
            }
        }

        customerCar.setCarTypeId(selectCar.get("carTypeId"));
        customerCar.setCustomerTransactionId(transaction.getId());

        customerCar.setLicenseNumber((String) sourceObj.get("licenseNumber"));
        customerCar.setVin((String) sourceObj.get("vin"));
        customerCar.setCustomerId(customerDemand.getCustomerId());
        customerCar.setEvaluatePrice((Double) sourceObj.get("assessmentPrice"));
        CarType carType = target.findOne(new Query(Criteria.where("_id").is(selectCar.get("carTypeId"))), CarType.class);
        if (carType != null) {
            customerCar.setMl(carType.getMl());
            customerCar.setGuidePrice(carType.getPrice());
        }else {
            customerCar.setMl("");
            customerCar.setGuidePrice(0.0);
        }
        target.save(customerCar);
        order.setCustomerCarId(customerCar.getId());

        CustomerLoan customerLoan = new CustomerLoan();
        BasicDBObject salePrice = (BasicDBObject) sourceObj.get("salePrice");
        customerLoan.setCustomerId(customerDemand.getCustomerId());
        customerLoan.setCustomerTransactionId(transaction.getId());
        customerLoan.setDownPayment((Double) salePrice.get("downPayment"));
        if ((Double) salePrice.get("downPaymentRatio") > 0){
            customerLoan.setDownPaymentRatio((Double) salePrice.get("downPaymentRatio"));
        } else {
            //  计算首付比例
            Double downPaymentRatio = salePrice.getDouble("downPayment") * 100 / (salePrice.getDouble("limitAmount") + salePrice.getDouble("downPayment")) ;
            DecimalFormat df = new DecimalFormat("#00.00");
            String format = df.format(downPaymentRatio);
            downPaymentRatio = Double.valueOf(format);
            customerLoan.setDownPaymentRatio(downPaymentRatio);
        }

        customerLoan.setCreditAmount((Double) salePrice.get("limitAmount"));
        if ((Double) salePrice.get("creditRatio") > 0){
            customerLoan.setCreditRatio((Double) salePrice.get("creditRatio"));
        } else {
            //  计算贷款比例
            Double creditRatio = salePrice.getDouble("limitAmount") * 100 / (salePrice.getDouble("limitAmount") + salePrice.getDouble("downPayment")) ;
            DecimalFormat df = new DecimalFormat("#00.00");
            String format = df.format(creditRatio);
            creditRatio = Double.valueOf(format);
            customerLoan.setCreditRatio(creditRatio);
        }

        customerLoan.setChargePaymentWay((String) sourceObj.get("chargePaymentWay"));
        customerLoan.setRealPrice((Double) sourceObj.get("realPrice"));
        customerLoan.setReceiptPrice((Double) sourceObj.get("receiptPrice"));
        customerLoan.setBankFeeAmount((Double) salePrice.get("charge"));
        customerLoan.setLoanServiceFee((Double) salePrice.get("serviceFee"));
        customerLoan.setApprovedCreditAmount((Double) salePrice.get("approvedCreditAmount"));
        customerLoan.setCompensatoryAmount((Double) salePrice.get("compensatoryAmount"));
        customerLoan.setSwipingAmount(customerLoan.getCreditAmount());
        customerLoan.setCompensatoryInterest((Boolean) salePrice.get("compensatoryInterest") ? 1 : 0);
        customerLoan.setIsNeedPayment(customerLoan.getCompensatoryInterest() == 0 ? carDealer.getPaymentPolicy().getBusiness() : carDealer.getPaymentPolicy().getDiscount()); // loan.setIsNeedPayment(loan.getCompensatoryInterest() == 0 ? carDealer.getPaymentPolicy().getBusiness() : carDealer.getPaymentPolicy().getDiscount());//是否垫资
        customerLoan.setApplyAmount((customerLoan.getCreditAmount()!=null?customerLoan.getCreditAmount():0) + (customerLoan.getDownPayment()!=null?customerLoan.getDownPayment():0));
        RateType rateType = new RateType();
        rateType.setMonths(salePrice.getInt("creditMonths"));
        rateType.setRatio((salePrice.getDouble("fuzeChargeRatio")) / 100);
        customerLoan.setRateType(rateType);
        try{
            Map map = Main.actCalculateCompensatoryWay(customerLoan.getBankFeeAmount(), customerLoan.getCompensatoryAmount(), customerLoan.getRateType().getMonths());
            if (map != null && !"".equals((String) map.get("compensatoryWay"))) {
                customerLoan.setCompensatoryWay((String) map.get("compensatoryWay"));
                customerLoan.setCompensatoryMonth((Integer) map.get("compensatoryMonth"));
            }
        }catch (Exception e){
            try {
                throw new Exception("actCalculateCompensatoryWay error", e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        target.save(customerLoan);

        order.setCustomerLoanId(customerLoan.getId());
        order.setDealerEmployeeId(customerDemand.getDealerEmployeeId());
        order.setCarNumber(sourceObj.containsField("carNumber") ? sourceObj.getInt("carNumber") : 0);
        order.setBuyCarNumber(sourceObj.containsField("buyCarNumber") ? sourceObj.getInt("buyCarNumber") : 0);
        order.setPrintNeedEarningProof((Boolean) sourceObj.get("printNeedEarningProof") ? 1 : 0);
        order.setReplaceStartCard((Boolean) sourceObj.get("replaceStartCard") ? 1 : 0);
        //order.setPickCarDate((String) sourceObj.get("printNeedEarningProof"));
        order.setIsStraight(0);

        //迁移费用项
        List<FeeValueBean> feeItems = new ArrayList<>();
        if (salePrice.get("incidental") != null) {
            FeeValueBean feeValueBean = new FeeValueBean();
            feeValueBean.setCode("INCIDENTAL");
            feeValueBean.setName("杂费");
            feeValueBean.setFee((Double) salePrice.get("incidental"));
            feeItems.add(feeValueBean);
        }
        if (salePrice.get("riskDeposit") != null) {
            FeeValueBean feeValueBean = new FeeValueBean();
            feeValueBean.setCode("RISKDEPOSIT");
            feeValueBean.setName("风险押金");
            feeValueBean.setFee((Double) salePrice.get("riskDeposit"));
            feeItems.add(feeValueBean);
        }
        if (salePrice.get("guaranteeFee") != null) {
            FeeValueBean feeValueBean = new FeeValueBean();
            feeValueBean.setCode("GUARANTEEFEE");
            feeValueBean.setName("担保服务费");
            feeValueBean.setFee((Double) salePrice.get("guaranteeFee"));
            feeItems.add(feeValueBean);
        }
        if (salePrice.get("loaningService") != null) {
            FeeValueBean feeValueBean = new FeeValueBean();
            feeValueBean.setCode("DIANZIFEE");
            feeValueBean.setName("垫资服务费");
            feeValueBean.setFee((Double) salePrice.get("loaningService"));
            feeItems.add(feeValueBean);
        }
        order.setFeeItemList(feeItems);
        if (sourceObj.getInt("status") == 11) {
            transaction.setStatus(11);
        }
        if (order.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
            transaction.setStatus(18);
        }
        CustomerLoan cardemandloan = target.findOne(new Query(Criteria.where("_id").is(new ObjectId(customerDemand.getCustomerLoanId()))), CustomerLoan.class);
        if (cardemandloan != null) {
            cardemandloan.setChargePaymentWay(customerLoan.getChargePaymentWay());
            cardemandloan.setRateType(rateType);
            cardemandloan.setBankFeeAmount(customerLoan.getBankFeeAmount());
            target.save(cardemandloan);
        }
        target.save(transaction);
        Map map = new HashMap<>();
        map.put(DataMigration.SAVED, DataMigration.YES);
        System.out.println("-----------------------------【PurchaseCarOrder】迁移完成-----------------------------");
        return map;
    }

}
