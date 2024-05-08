package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.creditcar.bean.SwipingCardTrusteeBean;
import com.fuze.bcp.api.creditcar.service.ISwipingCardTrusteeBizService;
import com.fuze.bcp.api.customer.bean.CustomerCardBean;
import com.fuze.bcp.api.customer.bean.CustomerRepaymentBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.BankCardApply;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.creditcar.domain.SwipingCardTrustee;
import com.fuze.bcp.creditcar.service.IBankCardApplyService;
import com.fuze.bcp.creditcar.service.IOrderService;
import com.fuze.bcp.creditcar.service.ISwipingCardService;
import com.fuze.bcp.creditcar.service.ISwipingCardTrusteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 受托支付(代刷卡)服务接口
 * Created by Lily on 2017/10/16.
 */
@Service
public class BizSwipingCardTrusteeService implements ISwipingCardTrusteeBizService {

    @Autowired
    IBankCardApplyService iBankCardApplyService;

    @Autowired
    ISwipingCardTrusteeService iSwipingCardTrusteeService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    /**
     * 初始化代刷卡信息(监控卡业务刷卡功能)
     * @param bankCardApplyId
     * @return
     */
    @Override
    public ResultBean<SwipingCardTrusteeBean> actCreateSwipingCardTrustee(String bankCardApplyId) {
        BankCardApply bankCardApply = iBankCardApplyService.getOne(bankCardApplyId);
        CustomerCardBean customerCardBean = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(bankCardApply.getCustomerTransactionId()).getD();
        if(bankCardApply != null){
            SwipingCardTrustee swipingCardTrustee = iSwipingCardTrusteeService.findByCustomerTransactionId(bankCardApply.getCustomerTransactionId());
            if(swipingCardTrustee == null){
                swipingCardTrustee = new SwipingCardTrustee();
                swipingCardTrustee.setBusinessTypeCode(bankCardApply.getBusinessTypeCode());
                swipingCardTrustee.setLoginUserId(bankCardApply.getLoginUserId());
                swipingCardTrustee.setEmployeeId(bankCardApply.getEmployeeId());
                swipingCardTrustee.setCustomerId(bankCardApply.getCustomerId());
                swipingCardTrustee.setCustomerTransactionId(bankCardApply.getCustomerTransactionId());
                swipingCardTrustee.setCarDealerId(bankCardApply.getCarDealerId());
                swipingCardTrustee.setPayAmount(bankCardApply.getSwipingMoney());
                swipingCardTrustee.setPayTime(bankCardApply.getSwipingTrusteeTime());
                if(customerCardBean != null){
                    swipingCardTrustee.setCardNumber(customerCardBean.getCardNo());
                }
                swipingCardTrustee = iSwipingCardTrusteeService.save(swipingCardTrustee);
                CustomerRepaymentBean customerRepaymentBean = this.updateCustomerRepayment(swipingCardTrustee,bankCardApply);
            }
        }
        return null;
    }

    /**
     * 更新还款信息
     * @param swipingCardTrustee
     * @param bankCardApply
     * @return
     */
    private CustomerRepaymentBean updateCustomerRepayment(SwipingCardTrustee swipingCardTrustee,BankCardApply bankCardApply) {
        //更新还款信息
        CustomerRepaymentBean customerRepaymentBean = iCustomerBizService.actGetCustomerRepaymentByCustomerTransactionId(swipingCardTrustee.getCustomerTransactionId()).getD();
        //还款信息存在则更新，不存在则创建
        if(customerRepaymentBean == null){
            customerRepaymentBean = new CustomerRepaymentBean();
            customerRepaymentBean.setCustomerId(swipingCardTrustee.getCustomerId());
            customerRepaymentBean.setCustomerTransactionId(swipingCardTrustee.getCustomerTransactionId());
            PurchaseCarOrder order = iOrderService.findByCustomerTransactionId(swipingCardTrustee.getCustomerTransactionId());
            if (order != null) {
                //首期还款额
                customerRepaymentBean.setFirstRepayment(order.getRepaymentAmountFirstMonth() != null ? Double.parseDouble(order.getRepaymentAmountFirstMonth()) : 0.0);
                //月还款额
                customerRepaymentBean.setMonthRepayment(order.getRepaymentAmountFirstMonth() != null ? Double.parseDouble(order.getRepaymentAmountMonthly()) : 0.0);
                //总还款额
                customerRepaymentBean.setTotalRepayment(order.getRepaymentAmountSum() != null ? Double.parseDouble(order.getRepaymentAmountSum()) : 0.0);
            }
            customerRepaymentBean.setRepayment(bankCardApply.getDefaultReimbursement());
            //TODO 代刷卡的还款方式暂不确定
            customerRepaymentBean = iCustomerBizService.actSaveCustomerRepayment(customerRepaymentBean).getD();
        }else{
            customerRepaymentBean.setCustomerTransactionId(swipingCardTrustee.getCustomerTransactionId());
            customerRepaymentBean.setCustomerId(swipingCardTrustee.getCustomerId());
            customerRepaymentBean.setCustomerTransactionId(swipingCardTrustee.getCustomerTransactionId());
            PurchaseCarOrder order = iOrderService.findByCustomerTransactionId(bankCardApply.getCustomerTransactionId());
            if (order != null) {
                //首期还款额
                customerRepaymentBean.setFirstRepayment(order.getRepaymentAmountFirstMonth() != null ? Double.parseDouble(order.getRepaymentAmountFirstMonth()) : 0.0);
                //月还款额
                customerRepaymentBean.setMonthRepayment(order.getRepaymentAmountFirstMonth() != null ? Double.parseDouble(order.getRepaymentAmountMonthly()) : 0.0);
            }
            customerRepaymentBean.setRepayment(bankCardApply.getDefaultReimbursement());
            customerRepaymentBean = iCustomerBizService.actSaveCustomerRepayment(customerRepaymentBean).getD();
        }
        return customerRepaymentBean;
    }
}
