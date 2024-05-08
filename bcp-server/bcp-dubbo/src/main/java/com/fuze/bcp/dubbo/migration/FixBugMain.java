package com.fuze.bcp.dubbo.migration;

import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.bd.domain.Employee;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.PayAccount;
import com.fuze.bcp.cardealer.domain.CarDealer;
import com.fuze.bcp.creditcar.domain.AppointPayment;
import com.fuze.bcp.creditcar.domain.CarRegistry;
import com.fuze.bcp.creditcar.domain.DMVPledge;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.customer.domain.CustomerLoan;
import com.fuze.bcp.dubbo.migration.mongo.MongoConnect;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class FixBugMain {

    public static void main(String[] args) throws Exception {
        //MongoTemplate target = MongoConnect.getMongoTemplate("test.fuzefenqi.com", 28018, "fuzetest", "admin", "FuzefenqiV11", "bcp_migration");
         MongoTemplate target = MongoConnect.getMongoTemplate("erp.fuzefenqi.com", 39299, "mongolive", "admin", "FuzefenqiPa88word", "bcp_v11");
        List<Employee> employees = target.find(new Query(), Employee.class);
        for (Employee employee : employees) {
            String id = employee.getId();
            employee.setWxUserId(id);
            target.save(employee);
        }
    }

    public static void actCreateDMVPledge(MongoTemplate target, String carRegistryId) {
        CarRegistry carRegistry = target.findOne(new Query(Criteria.where("_id").is(new ObjectId(carRegistryId))), CarRegistry.class);
        if (carRegistry != null) {
            if (carRegistry.getApproveStatus() != ApproveStatus.APPROVE_PASSED) {
                return;
            }
            DMVPledge dmvPledge = target.findOne(new Query(Criteria.where("customerTransactionId").is(carRegistry.getCustomerTransactionId())), DMVPledge.class);
            if (dmvPledge == null) {
                dmvPledge = new DMVPledge();
                dmvPledge.setBusinessTypeCode(carRegistry.getBusinessTypeCode());
                dmvPledge.setLoginUserId(carRegistry.getLoginUserId());
                dmvPledge.setEmployeeId(carRegistry.getEmployeeId());
                dmvPledge.setCustomerId(carRegistry.getCustomerId());
                dmvPledge.setCustomerTransactionId(carRegistry.getCustomerTransactionId());
                dmvPledge.setCarDealerId(carRegistry.getCarDealerId());
                dmvPledge.setStatus(0);
                target.save(dmvPledge);
            }
        }
    }

    public static void liucheng(String[] args) throws Exception {

        MongoTemplate target = MongoConnect.getMongoTemplate("erp.fuzefenqi.com", 39299, "mongolive", "admin", "FuzefenqiPa88word", "bcp_v11");
        PurchaseCarOrder purchaseCarOrder = target.findOne(new Query(Criteria.where("_id").is(new ObjectId("5a599ce7cd7d5741bc945718"))), PurchaseCarOrder.class);
        if (purchaseCarOrder == null) {

        }
        if (purchaseCarOrder.getApproveStatus() != ApproveStatus.APPROVE_PASSED) {

        }
        CustomerLoan customerLoanBean = target.findOne(new Query(Criteria.where("_id").is(new ObjectId(purchaseCarOrder.getCustomerLoanId()))), CustomerLoan.class);
        CustomerTransaction customerTransaction = target.findOne(new Query(Criteria.where("_id").is(new ObjectId(purchaseCarOrder.getCustomerTransactionId()))), CustomerTransaction.class);
        CarDealer carDealerBean = target.findOne(new Query(Criteria.where("_id").is(new ObjectId(customerTransaction.getCarDealerId()))), CarDealer.class);
        //从垫资政策获取是否需要垫资
        if (customerLoanBean.getIsNeedPayment() != null && customerLoanBean.getIsNeedPayment() == 1) {
            /*//不贴息的时候才会创建预约垫资单
            if (customerLoanBean.getCompensatoryAmount() == null || customerLoanBean.getCompensatoryInterest() == 0) {}*/
            //TODO 在创建单据时需要将一些不可更改的数据赋值
            AppointPayment appointPayment = null;
            if (appointPayment == null) {
                appointPayment = new AppointPayment();
                appointPayment.setBusinessTypeCode(purchaseCarOrder.getBusinessTypeCode());
                appointPayment.setLoginUserId(purchaseCarOrder.getLoginUserId());
                appointPayment.setEmployeeId(purchaseCarOrder.getEmployeeId());
                appointPayment.setCustomerId(purchaseCarOrder.getCustomerId());
                appointPayment.setCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
                //垫资时可以更换渠道
                appointPayment.setCarDealerId(customerTransaction.getCarDealerId());
                //创建时获取当前渠道的默认账户
                List<PayAccount> payAccounts = carDealerBean.getPayAccounts();
                if (payAccounts != null && !payAccounts.isEmpty()) {
                    for (PayAccount payAccount : payAccounts) {
                        if (payAccount.getDefaultAccount() == PayAccount.ACCOUTCHECK_ALL) {
                            appointPayment.setPayAccount(payAccount);
                        }
                    }
                }
                // 10-18 初始化时获取渠道的支付方式和垫资时间
                appointPayment.setChargeParty(carDealerBean.getPaymentMethod());
                // 10-31 初始化垫资金额(垫资策略下只考虑垫资政策，正常垫资直接调接口)
                appointPayment.setAppointPayAmount(actGetAppointPaymentAmount(appointPayment.getCustomerTransactionId(), appointPayment.getChargeParty().toString(), purchaseCarOrder, customerLoanBean));
                /*if(customerLoanBean.getCompensatoryInterest() == 0 ){//商贷
                    appointPayment.setAppointPayAmount(this.actGetAppointPaymentAmount(appointPayment.getCustomerTransactionId(),appointPayment.getChargeParty().toString()).getD());
                }else if(customerLoanBean.getCompensatoryInterest() == 1){//贴息
                    appointPayment.setAppointPayAmount(customerLoanBean.getCreditAmount());
                }*/

                if (purchaseCarOrder.getBusinessTypeCode().equals("OC")) {
                    appointPayment.setAdvancedPay(Integer.parseInt(carDealerBean.getPaymentOldTime()));
                } else {
                    appointPayment.setAdvancedPay(Integer.parseInt(carDealerBean.getPaymentNewTime()));
                }

                // 10-12 增加 是否贴息状态 和 贴息金额
                appointPayment.setCompensatoryAmount(customerLoanBean.getCompensatoryAmount());
                appointPayment.setDataStatus(DataStatus.TEMPSAVE);
                target.save(appointPayment);
                //TODO 垫资完成之后创建还款单(有发生垫资情况)，不贴息也没有垫资情况创建刷卡单
            }
        }


    }


    public static Double actGetAppointPaymentAmount(String tid, String chargeParty, PurchaseCarOrder purchaseCarOrder, CustomerLoan customerLoanBean) {
        /***************垫资金额计算方法***************/
        // NC:差额： 趸交(垫资金额 = 贷款金额 -贷款服务费-杂费)（CreditAmount - LoanServiceFee - Sum(purchaseCarOrder.getFeeItemList()))
        // OC:差额： 趸交(垫资金额 = 贷款金额 -贷款服务费-杂费 - 银行手续费)（CreditAmount - LoanServiceFee - Sum(purchaseCarOrder.getFeeItemList()) - bankFeeAmount)
        // 差额： 分期(贷款金额 -贷款服务费 -杂费 )（CreditAmount - LoanServiceFee - Sum(purchaseCarOrder.getFeeItemList()))
        // 全额：（CreditAmount)
        if (chargeParty.equals("0")) { //差额
            Double totalFeeAmount = 0.0;
            Double amount = 0.0;
            for (FeeValueBean feeValue : purchaseCarOrder.getFeeItemList()) {
                totalFeeAmount += feeValue.getFee();
            }
            if (purchaseCarOrder.getBusinessTypeCode().equals("NC")) {
                if (customerLoanBean.getChargePaymentWay().equals("WHOLE")) { //趸交
                    amount = customerLoanBean.getCreditAmount() - customerLoanBean.getLoanServiceFee() - totalFeeAmount;
                } else { //分期
                    amount = customerLoanBean.getCreditAmount() - customerLoanBean.getLoanServiceFee() - totalFeeAmount;
                }
            } else if (purchaseCarOrder.getBusinessTypeCode().equals("OC")) {
                if (customerLoanBean.getChargePaymentWay().equals("WHOLE")) { //趸交
                    amount = customerLoanBean.getCreditAmount() - customerLoanBean.getLoanServiceFee() - totalFeeAmount - customerLoanBean.getBankFeeAmount();
                } else { //分期
                    amount = customerLoanBean.getCreditAmount() - customerLoanBean.getLoanServiceFee() - totalFeeAmount;
                }
            }
            return amount;
        } else {
            return customerLoanBean.getCreditAmount();
        }
    }
}
