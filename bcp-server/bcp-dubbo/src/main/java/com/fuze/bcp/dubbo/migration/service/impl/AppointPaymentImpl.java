package com.fuze.bcp.dubbo.migration.service.impl;

import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.PayAccount;
import com.fuze.bcp.creditcar.domain.*;
import com.fuze.bcp.customer.domain.CustomerLoan;
import com.fuze.bcp.dubbo.migration.DataMigration;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/10/19.
 */
public class AppointPaymentImpl implements DataMigration {
    @Override
    public Map<String, String> billDataMigration(CustomerDemand customerDemand, CustomerTransaction transaction, BasicDBObject sourceObj, BaseBillEntity targetObj, MongoTemplate source, MongoTemplate target) {
        Map map = new HashMap<>();
        PurchaseCarOrder order = target.findOne(new Query(Criteria.where("customerTransactionId").is(transaction.getId())), PurchaseCarOrder.class);
        CustomerLoan customerLoan = target.findOne(new Query(Criteria.where("_id").is(order.getCustomerLoanId())), CustomerLoan.class);

        AppointPayment payment = (AppointPayment) targetObj;
        payment.setPickupDate(sourceObj.get("pickDate") != null ? sourceObj.get("pickDate").toString() : null);
        payment.setAppointPayTime(sourceObj.get("payTime") != null ? sourceObj.get("payTime").toString() : null);
        payment.setRegistryDate(sourceObj.get("registryDate") != null ? sourceObj.get("registryDate").toString() : null);
        payment.setAdvancedPay((Boolean) sourceObj.get("advancedPay") ? 1 : 2);
        payment.setChargeParty(sourceObj.getInt("chargeParty"));

        Query query = new Query();
        query.addCriteria(Criteria.where("pickupCarId").is(sourceObj.get("_id").toString()));
        BasicDBObject carpayment = source.findOne(query, BasicDBObject.class, "fi_carpayment");
        if (carpayment.getInt("approveStatus") == 2) {
            payment.setStatus(5); //财务已支付
            payment.setApproveStatus(ApproveStatus.APPROVE_PASSED);
        } else {
            payment.setApproveStatus(ApproveStatus.APPROVE_INIT);
            payment.setStatus(AppointPaymentBean.APPOINTPAYMENTSTATUS_INIT);
        }
        Double payAmount = carpayment.containsField("payAmount") ? carpayment.getDouble("payAmount") : 0;
        if (customerLoan.getCompensatoryInterest() == 0 ) { // 垫资
            payment.setAppointPayAmount(payAmount);
            payment.setPayTime(carpayment.getString("payTime"));
            BasicDBObject payAccountMap = (BasicDBObject) carpayment.get("receiveAccount");
            if (payAccountMap != null) {
                PayAccount payAccount = new PayAccount();
                payAccount.setAccountName(payAccountMap.getString("accountName"));
                payAccount.setAccountNumber((String) payAccountMap.get("accountNumber"));
                payAccount.setBankName((String) payAccountMap.get("bankName"));
                payAccount.setAccountType(payAccountMap.getInt("accountType"));
                payAccount.setAccountWay(payAccountMap.getInt("accountWay"));
                payAccount.setDefaultAccount(payAccountMap.getBoolean("defaultAccount") ? 0 : 1);
                payment.setPayAccount(payAccount);
            }
            Map<String, String> map1 = new HashMap<>();
            map1.put("ACCOUNT_NAME","富择");
            map1.put("ACCOUNT_NUMBER","56565655454684654646");
            map1.put("BANK_NAME","工商银行");
            PayAccount payAccount = new PayAccount();
            payAccount.setAccountName(map1.get("ACCOUNT_NAME"));
            payAccount.setAccountNumber(map1.get("ACCOUNT_NUMBER"));
            payAccount.setBankName(map1.get("BANK_NAME"));
            CustomerFeeBill customerFee = new CustomerFeeBill();
            customerFee.setCustomerTransactionId(transaction.getId());
            customerFee.setLoanServiceFee(customerLoan.getLoanServiceFee());
            customerFee.setPayAccount(payAccount);
            List<FeeValueBean> feeItemList = order.getFeeItemList();
            Double fee = 0.00;
            if (feeItemList != null) {
                for (FeeValueBean feeValue : feeItemList) {
                    fee += feeValue.getFee();
                }
            }
            customerFee.setTotalFee(customerLoan.getLoanServiceFee() + fee);
            customerFee.setFeeItemList(feeItemList);
            target.save(customerFee);
            map.put(DataMigration.SAVED, DataMigration.YES);
        } else {
            map.put(DataMigration.SAVED, DataMigration.NO);
        }

            //payment.setCompensatoryAmount();
        System.out.println("-----------------------------【AppointPayment】迁移完成-----------------------------");
        return map;
    }
}
