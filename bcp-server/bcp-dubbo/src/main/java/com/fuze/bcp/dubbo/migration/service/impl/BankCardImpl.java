package com.fuze.bcp.dubbo.migration.service.impl;

import com.fuze.bcp.api.creditcar.bean.CardActionRecord;
import com.fuze.bcp.creditcar.domain.BankCardApply;
import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.customer.domain.CustomerCard;
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
 * Created by CJ on 2017/10/18.
 */
public class BankCardImpl implements DataMigration {

    /**
     * 卡业务数据迁移
     * @param customerDemand
     * @param sourceObj
     * @param targetObj
     * @param source
     * @param target
     * @return
     */
    @Override
    public Map<String, String> billDataMigration(CustomerDemand customerDemand, CustomerTransaction transaction, BasicDBObject sourceObj, BaseBillEntity targetObj, MongoTemplate source, MongoTemplate target) {
        PurchaseCarOrder order = target.findOne(new Query(Criteria.where("customerTransactionId").is(transaction.getId())), PurchaseCarOrder.class);
        CustomerLoan customerLoan = target.findOne(new Query(Criteria.where("id").is(order.getCustomerLoanId())),CustomerLoan.class);
        CustomerCard customerCard = new CustomerCard();
        BankCardApply bankCardApply = (BankCardApply)targetObj;
        //卡信息
        customerCard.setCustomerTransactionId(transaction.getId());
        customerCard.setCustomerId(bankCardApply.getCustomerId());
        customerCard.setCardNo(sourceObj.get("CardNo") != null ? (String)sourceObj.get("CardNo") : null);
        customerCard.setExpireDate(sourceObj.get("expireDate") != null ? (String)sourceObj.get("expireDate") : null);
        customerCard.setCvv(sourceObj.get("cvv") != null ? (String)sourceObj.get("cvv") : null);
        customerCard.setInitPassword(sourceObj.get("initPassword") != null ? (String)sourceObj.get("initPassword") : null);
        target.save(customerCard);
        //卡业务信息
        bankCardApply.setApplyTime(sourceObj.get("applyTime") != null ? (String)sourceObj.get("applyTime") : null);
        bankCardApply.setTakeTime(sourceObj.get("takeTime") != null ? (String)sourceObj.get("takeTime") : null);
        bankCardApply.setActivateTime(sourceObj.get("activateTime") != null ? (String)sourceObj.get("activateTime") : null);
        bankCardApply.setReplaceActivateTime(sourceObj.get("replaceActiveTime") != null ? (String)sourceObj.get("replaceActiveTime") : null);
        // 代起卡人
        bankCardApply.setReplaceActivateName(sourceObj.get("replaceActiveName") != null ? (String)sourceObj.get("replaceActiveName") : null);
        bankCardApply.setSwipingMoney(sourceObj.get("swipingMoney") != null && !sourceObj.get("swipingMoney").equals("") ? new Double(sourceObj.get("swipingMoney").toString()) : null);
        bankCardApply.setSwipingPeriods(customerLoan.getRateType().getMonths());
        bankCardApply.setReceiveDiscountTime(sourceObj.get("receiveDiscountTime") != null ? (String)sourceObj.get("receiveDiscountTime") : null);
        bankCardApply.setSwipingShopTime(sourceObj.get("swipingShopTime") != null ? (String)sourceObj.get("swipingShopTime") : null);
        bankCardApply.setSwipingTrusteeTime(sourceObj.get("swipingTrusteeTime") != null ? (String)sourceObj.get("swipingTrusteeTime") : null);
        bankCardApply.setReceiveTrusteeTime(sourceObj.get("receiveTrusteeTime") != null ? (String)sourceObj.get("receiveTrusteeTime") : null);
        bankCardApply.setCancelCardTime(sourceObj.get("cancelCardTime") != null ? (String)sourceObj.get("cancelCardTime") : null);
        bankCardApply.setChangeAmountTime(sourceObj.get("changeAmountTime") != null ? (String)sourceObj.get("changeAmountTime") : null);
        bankCardApply.setSwipingName(sourceObj.get("swipingName") != null ? (String)sourceObj.get("swipingName") : null);
        bankCardApply.setReceiveCardName(sourceObj.get("receiveCardName") != null ? (String)sourceObj.get("receiveCardName") : null);
        bankCardApply.setFirstReimbursement(sourceObj.get("firstReimbursement") != null ? (String)sourceObj.get("firstReimbursement") : null);
        bankCardApply.setDefaultReimbursement(sourceObj.get("defaultReimbursement") != null ? new Double(sourceObj.get("defaultReimbursement").toString()).intValue() : null);
        bankCardApply.setBillingDate(5);
        bankCardApply.setActionRecords(sourceObj.get("actionRecords") != null ? (List<CardActionRecord>)sourceObj.get("actionRecords") : null);
        bankCardApply.setApproveStatus(sourceObj.getInt("approveStatus"));
        if (sourceObj.containsField("status") && sourceObj.getInt("status") == 5) {
            bankCardApply.setStatus(6);
        } else if (sourceObj.containsField("status") && sourceObj.getInt("status") == 6) {
            bankCardApply.setStatus(5);
        } else {
            bankCardApply.setStatus(sourceObj.get("status") != null ? new Double(sourceObj.get("status").toString()).intValue() : null);
        }
        if (customerLoan.getCompensatoryInterest() == 1) { //创建渠道刷卡和还款计划

        }
        Map map = new HashMap<>();
        map.put(DataMigration.SAVED, DataMigration.YES);
        System.out.println("-----------------------------【BankCardApply】迁移完成-----------------------------");
        return map;
    }

}
