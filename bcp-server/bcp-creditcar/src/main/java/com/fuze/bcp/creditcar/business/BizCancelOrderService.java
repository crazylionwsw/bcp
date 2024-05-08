package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.creditcar.bean.CancelOrderBean;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.service.IBankCardApplyBizService;
import com.fuze.bcp.api.creditcar.service.ICancelOrderBizService;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.creditcar.domain.*;
import com.fuze.bcp.creditcar.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by GQR on 2017/8/19.
 */
@Service
public class BizCancelOrderService implements ICancelOrderBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCancelOrderService.class);

    @Autowired
    MappingService mappingService;

    @Autowired
    ICancelOrderService iCancelOrderService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ISwipingCardService iSwipingCardService;

    @Autowired
    ISwipingCardTrusteeService iSwipingCardTrusteeService;

    @Autowired
    IDmvpledgeService iDmvpledgeService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    @Autowired
    MessageService messageService;

    /**
     * 右边框根据id显示
     * @param id
     * @return
     */
    @Override
    public ResultBean<CancelOrderBean> actGetCancelOrder(String id) {
        CancelOrder cancelOrder=iCancelOrderService.getOne(id);
        if (cancelOrder == null){
            return ResultBean.getFailed();
        }
        String code=cancelOrder.getBillTypeCode();
        BillTypeBean billType=iBaseDataBizService.actGetBillType(code).getD();
        CancelOrderBean cancelOrderBean=mappingService.map(cancelOrder,CancelOrderBean.class);
        cancelOrderBean.setBillType(billType);
        return ResultBean.getSucceed().setD(cancelOrderBean);
    }

    @Override
    public ResultBean<CancelOrderBean> actSearchCancelOrders(String userId, SearchBean searchBean) {
        Page<CancelOrder> cancelOrders = iCancelOrderService.findAllBySearchBean(CancelOrder.class, searchBean, SearchBean.STAGE_TRANSACTION, userId);
        return ResultBean.getSucceed().setD(mappingService.map(cancelOrders,CancelOrderBean.class));
    }

    @Override
    public ResultBean<CancelOrderBean> actGetCancelOrderByTransactionId(String transactionId) {
        CancelOrder cancelOrder=iCancelOrderService.findAvailableOneByCustomerTransactionId(transactionId);
        if (cancelOrder == null){
            return ResultBean.getFailed();
        }
        String code=cancelOrder.getBillTypeCode();
        BillTypeBean billType=iBaseDataBizService.actGetBillType(code).getD();
        CancelOrderBean cancelOrderBean=mappingService.map(cancelOrder,CancelOrderBean.class);
        cancelOrderBean.setBillType(billType);
        return ResultBean.getSucceed().setD(cancelOrderBean);
    }

    @Override
    public ResultBean<CancelOrderBean> actSaveCancelOrder(CancelOrderBean cancelOrderBean) {
        return null;
    }

    @Override
    public ResultBean<CancelOrderBean> actSubmitCancelOrder(String tid, String comment) {

        ResultBean result = iCustomerTransactionBizService.actGetEditableTransaction(tid);
        if (result.failed()) return result;

        CustomerTransactionBean transaction = (CustomerTransactionBean) result.getD();

        CancelOrder oldCancelOrder = iCancelOrderService.findByCustomerTransactionId(tid);
        if(oldCancelOrder != null && oldCancelOrder.getApproveStatus() != ApproveStatus.APPROVE_REJECT) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CANCEL_NOSUBMIT"));
        }
        //校验是否可以提交
        ResultBean<CancelOrderBean> resultBean = this.checkAllowSubmit(transaction.getId());
        if(resultBean.failed()) return resultBean;

        CancelOrder cancelOrder = new CancelOrder();
        //构建CancelOrder对象，从CarDemand取得所需要的其它字段值
        cancelOrder.setCustomerTransactionId(transaction.getId());
        cancelOrder.setLoginUserId(transaction.getLoginUserId());
        cancelOrder.setCustomerId(transaction.getCustomerId());
        cancelOrder.setOrginfoId(transaction.getOrginfoId());
        cancelOrder.setEmployeeId(transaction.getEmployeeId());
        cancelOrder.setBusinessTypeCode(transaction.getBusinessTypeCode());
        cancelOrder.setCarDealerId(transaction.getCarDealerId());
        cancelOrder.setDataStatus(DataStatus.SAVE);
        cancelOrder.setCashSourceId(transaction.getCashSourceId());
        cancelOrder.setReason(comment);

        cancelOrder = iCancelOrderService.save(cancelOrder);

        transaction.setStatus(CustomerTransactionBean.TRANSACTION_CANCELLING); //修改交易状态
        iCustomerTransactionBizService.actSaveCustomerTransaction(transaction);

        //进入审批流
        SignInfo signInfo = new SignInfo(cancelOrder.getLoginUserId(), cancelOrder.getEmployeeId(), SignInfo.SIGN_PASS,SignInfo.FLAG_COMMIT, comment);
        String collectionMame = null;
        try {
            collectionMame = CancelOrder.getMongoCollection(cancelOrder);
        } catch (Exception e) {
            // TODO: 2017/9/9
            e.printStackTrace();
        }
        result = iWorkflowBizService.actSubmit(cancelOrder.getBusinessTypeCode(), cancelOrder.getId(), cancelOrder.getBillTypeCode(), signInfo, collectionMame, null, transaction.getId());
        if (result.failed()) return result;

        return ResultBean.getSucceed().setD(mappingService.map(cancelOrder, CancelOrderBean.class))
                .setM(messageService.getMessage("MSG_CANCEL_WAITINGCHECK"));
    }

    /**
     * 检查是否可以取消业务
     * @param transactionId
     * @return
     */
    private ResultBean<CancelOrderBean> checkAllowSubmit(String transactionId){
        //渠道刷卡不能取消业务
        SwipingCard swipingCard = iSwipingCardService.findByCustomerTransactionId(transactionId);
        if(swipingCard != null && swipingCard.getStatus() >= 1){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CANCEL_SWIPINGCARDEXIST"));
        }
        //代刷卡不能取消业务
        SwipingCardTrustee swipingCardTrustee = iSwipingCardTrusteeService.findByCustomerTransactionId(transactionId);
        if(swipingCardTrustee != null){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CANCEL_SWIPINGCARDTRUSTEEEXIST"));
        }
        //抵押不能取消业务
        DMVPledge dmvPledge = iDmvpledgeService.findByCustomerTransactionId(transactionId);
        if(dmvPledge != null && dmvPledge.getStatus() >= 1){
            //抵押资料已经签收，无法进行取消
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CANCEL_DMVPLEDGEEXIST"));
        }
        return ResultBean.getSucceed();
    }


    @Override
    public ResultBean<CancelOrderBean> actSignCancelOrder(String id, SignInfo signInfo) {
        //提交审核任务
        try {
            ResultBean<WorkFlowBillBean> resultBean = iWorkflowBizService.actSignBill(id, signInfo);
            if(resultBean.failed()){
                return ResultBean.getFailed().setM(resultBean.getM());
            }

            WorkFlowBillBean workFlow = (WorkFlowBillBean) resultBean.getD();

        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAILED_SIGN"));
        }

        //获取预约垫资信息
        CancelOrder cancelOrder = iCancelOrderService.getOne(id);
        CustomerTransactionBean transaction = iCustomerTransactionBizService.actFindCustomerTransactionById(cancelOrder.getCustomerTransactionId()).getD();
        if (cancelOrder.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
            BankCardApplyBean bankCardApplyBean = iBankCardApplyBizService.actFindBankCardApplyByTransactionId(cancelOrder.getCustomerTransactionId()).getD();
            if(bankCardApplyBean != null){
                //将卡业务流程直接销卡
                bankCardApplyBean.setStatus(BankCardApplyBean.BKSTATUS_CANCEL);
                iBankCardApplyBizService.actApprovedBankCardApply(bankCardApplyBean,BankCardApplyBean.BKSTATUS_CANCEL,-1,signInfo.getUserId());
            }
            //修改交易状态为CustomerTransactionBean.TRANSACTION_CANCELLED
            transaction.setStatus(CustomerTransactionBean.TRANSACTION_CANCELLED);
            iCustomerTransactionBizService.actSaveCustomerTransaction(transaction);

            //工作流
            iWorkflowBizService.actStopTransaction(transaction.getId(), signInfo.getComment(), signInfo.getUserId());

        }else if (cancelOrder.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
            //取消业务中拒绝后将业务状态改为进行中
            transaction.setStatus(CustomerTransactionBean.TRANSACTION_PROCESSING);
            iCustomerTransactionBizService.actSaveCustomerTransaction(transaction);
        }
        return ResultBean.getSucceed().setD(mappingService.map(cancelOrder, CancelOrderBean.class));
    }

    @Override
    public ResultBean<List<CancelOrderBean>> actGetCancelOrders(String loginUserId, Integer currentPage) {
        return null;
    }

}
