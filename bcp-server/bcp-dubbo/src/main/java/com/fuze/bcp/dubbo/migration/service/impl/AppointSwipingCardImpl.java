package com.fuze.bcp.dubbo.migration.service.impl;

import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentBean;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.PayAccount;
import com.fuze.bcp.creditcar.domain.*;
import com.fuze.bcp.customer.domain.CustomerCard;
import com.fuze.bcp.customer.domain.CustomerLoan;
import com.fuze.bcp.dubbo.migration.DataMigration;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.workflow.domain.WorkFlowBill;
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
public class AppointSwipingCardImpl implements DataMigration {

    @Override
    public Map<String, String> billDataMigration(CustomerDemand customerDemand, CustomerTransaction transaction, BasicDBObject sourceObj, BaseBillEntity targetObj, MongoTemplate source, MongoTemplate target) {
        Map map = new HashMap<>();
        PurchaseCarOrder order = target.findOne(new Query(Criteria.where("customerTransactionId").is(transaction.getId())), PurchaseCarOrder.class);
        BankCardApply bankCardApply = target.findOne(new Query(Criteria.where("customerTransactionId").is(transaction.getId())), BankCardApply.class);
        CustomerCard customerCard = target.findOne(new Query(Criteria.where("customerTransactionId").is(transaction.getId())), CustomerCard.class);

        if (order.getApproveStatus() != ApproveStatus.APPROVE_PASSED) {
            map.put(DataMigration.SAVED, DataMigration.NO);
            return map;
        }
        CustomerLoan customerLoan = target.findOne(new Query(Criteria.where("_id").is(order.getCustomerLoanId())), CustomerLoan.class);
        AppointSwipingCard swiping = (AppointSwipingCard) targetObj;
        swiping.setPickupDate(sourceObj.get("pickDate") != null ? sourceObj.get("pickDate").toString() : null);
        swiping.setAppointPayTime(sourceObj.get("payTime") != null ? sourceObj.get("payTime").toString() : null);
        BasicDBObject carpayment = source.findOne(new Query(Criteria.where("pickupCarId").is(sourceObj.get("_id").toString())), BasicDBObject.class, "fi_carpayment");
        if (customerLoan.getCompensatoryInterest() == 1) { // 贴息刷卡
            if (carpayment.getInt("approveStatus") == 2) { // 1.0 垫资通过
                // 1.设置预约刷卡单通过
                swiping.setStatus(2); //已确认
                swiping.setApproveStatus(2);
                // 2.创建渠道刷卡
                SwipingCard swipingCard = new SwipingCard();
                swipingCard.setCardNumber(customerCard.getCardNo());
                swipingCard.setPayTime(sourceObj.get("swipingShopTime") != null ? (String)sourceObj.get("swipingShopTime") : null);
                swipingCard.setPayAmount(customerLoan.getCreditAmount());
                if (bankCardApply.getSwipingShopTime() != null || bankCardApply.getSwipingTrusteeTime() != null) {
                    swipingCard.setStatus(2); //已确认
                    swipingCard.setApproveStatus(ApproveStatus.APPROVE_PASSED);
                } else {
                    swipingCard.setStatus(0);
                    swipingCard.setApproveStatus(ApproveStatus.APPROVE_INIT);
                }
                swipingCard.setBusinessTypeCode(order.getBusinessTypeCode());
                swipingCard.setTs(swiping.getTs());
                swipingCard.setCarDealerId(bankCardApply.getCarDealerId());
                swipingCard.setCashSourceId(bankCardApply.getCashSourceId());
                swipingCard.setCustomerId(bankCardApply.getCustomerId());
                swipingCard.setCustomerTransactionId(bankCardApply.getCustomerTransactionId());
                swipingCard.setEmployeeId(bankCardApply.getEmployeeId());
                swipingCard.setLoginUserId(bankCardApply.getLoginUserId());
                swipingCard.setOrginfoId(bankCardApply.getOrginfoId());
                target.save(swipingCard);
                WorkFlowBill workFlowBill = new WorkFlowBill();
                workFlowBill.setCollectionName("so_swiping_card");
                workFlowBill.setFlowCode(swipingCard.getBillTypeCode());
                workFlowBill.setSourceId(swipingCard.getId());
                workFlowBill.setTransactionId(customerDemand.getCustomerTransactionId());
                workFlowBill.setActivitiId(workFlowBill.getFlowCode() + "." + workFlowBill.getSourceId());
                workFlowBill.setBusinessTypeCode(swipingCard.getBusinessTypeCode());
                workFlowBill.setApproveStatus(swipingCard.getApproveStatus());
                workFlowBill.setDataStatus(1);
                workFlowBill.setTs(swipingCard.getTs());
                target.save(workFlowBill);
            } else {
                swiping.setStatus(AppointSwipingCardBean.APPOINTSWIPINGSTATUS_INIT); //待确认
                swiping.setApproveStatus(ApproveStatus.APPROVE_INIT);
            }
            swiping.setAppointPayAmount(customerLoan.getCreditAmount());
            Double payAmount = carpayment.containsField("payAmount") ? carpayment.getDouble("payAmount") : 0; // 是否有垫资金额
            if (payAmount > 0) {
                swiping.setIsNeedLoaning(1);
                AppointPayment payment = new AppointPayment();
                payment.setPickupDate(sourceObj.get("pickDate") != null ? sourceObj.get("pickDate").toString() : null);
                payment.setAppointPayTime(sourceObj.get("payTime") != null ? sourceObj.get("payTime").toString() : null);
                payment.setRegistryDate(sourceObj.get("registryDate") != null ? sourceObj.get("registryDate").toString() : null);
                payment.setAdvancedPay((Boolean) sourceObj.get("advancedPay") ? 1 : 2);
                payment.setChargeParty(sourceObj.getInt("chargeParty"));
                if (carpayment.getInt("approveStatus") == 2) {
                    payment.setStatus(5); //财务已支付
                    payment.setApproveStatus(ApproveStatus.APPROVE_PASSED);
                } else {
                    payment.setApproveStatus(ApproveStatus.APPROVE_INIT);
                    payment.setStatus(AppointPaymentBean.APPOINTPAYMENTSTATUS_INIT);
                }
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
                payment.setCompensatoryAmount(customerLoan.getCompensatoryAmount());
                payment.setNeedCompensatory(1);
                payment.setIsNeedLoaning(1);
                target.save(payment);
                //TODO: 创建渠道还款
                if (swiping.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
                    DealerRepayment dealerRepayment = new DealerRepayment();
                    dealerRepayment.setBusinessTypeCode(swiping.getBusinessTypeCode());
                    dealerRepayment.setCustomerId(swiping.getCustomerId());
                    dealerRepayment.setCarDealerId(swiping.getCarDealerId());
                    dealerRepayment.setTs(swiping.getTs());
                    dealerRepayment.setCashSourceId(swiping.getCashSourceId());
                    dealerRepayment.setLoginUserId(swiping.getLoginUserId());
                    dealerRepayment.setEmployeeId(swiping.getEmployeeId());
                    dealerRepayment.setOrginfoId(swiping.getOrginfoId());
                    dealerRepayment.setCustomerTransactionId(payment.getCustomerTransactionId());
                    dealerRepayment.setAmount(payAmount);
                    dealerRepayment.setStatus(DealerRepaymentBean.DEALERREPAYMENTSTATUS_INIT);
                    dealerRepayment.setApproveStatus(ApproveStatus.APPROVE_INIT);
                    target.save(dealerRepayment);
                }
            }else{
                swiping.setIsNeedLoaning(0);
            }
            map.put(DataMigration.SAVED, DataMigration.YES);
        } else {
            map.put(DataMigration.SAVED, DataMigration.NO);
        }

            //payment.setCompensatoryAmount();

        System.out.println("-----------------------------【AppointSwipingCard】迁移完成-----------------------------");
        return map;
    }
}
