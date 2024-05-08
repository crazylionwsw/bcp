package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.creditcar.bean.CustomerDemandBean;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.RejectCustomerBean;
import com.fuze.bcp.api.creditcar.service.ICustomerDemandBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.creditcar.service.IOrderBizService;
import com.fuze.bcp.api.creditcar.service.IRejectCustomerBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerRelation;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.creditcar.domain.RejectCustomer;
import com.fuze.bcp.creditcar.service.IRejectCustomerService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqw on 2017/12/18.
 */
@Service
public class BizRejectCustomerService implements IRejectCustomerBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizRejectCustomerService.class);

    @Autowired
    IRejectCustomerService iRejectCustomerService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerDemandBizService iCustomerDemandBizService;

    @Autowired
    IOrderBizService iOrderBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<RejectCustomerBean> actCreateRejectCustomerByDemandId(String demandId){
        CustomerDemandBean customerDemandBean = iCustomerDemandBizService.actFindCustomerDemandById(demandId).getD();
        if (customerDemandBean == null){
            logger.error(String.format(messageService.getMessage("MSG_CUSTOMERDEMAND_NOTFIND_ID"),demandId));
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CUSTOMERDEMAND_NOTFIND_ID"),demandId));
        }
        return this.actCreateRejectCustomerByTransactionId(customerDemandBean.getCustomerTransactionId(),customerDemandBean.getBillTypeCode());
    }

    @Override
    public ResultBean<RejectCustomerBean> actCreateRejectCustomerByOrderId(String orderId){
        PurchaseCarOrderBean purchaseCarOrderBean = iOrderBizService.actGetOrder(orderId).getD();
        if (purchaseCarOrderBean == null){
            logger.error(String.format(messageService.getMessage("MSG_ORDER_NULL_ID"),orderId));
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_ORDER_NULL_ID"),orderId));
        }
        return this.actCreateRejectCustomerByTransactionId(purchaseCarOrderBean.getCustomerTransactionId(),purchaseCarOrderBean.getBillTypeCode());
    }

    public ResultBean<RejectCustomerBean> actCreateRejectCustomerByTransactionId(String transactionId, String billTypeCode){

        //获取购车资质
        CustomerDemandBean customerDemandBean = iCustomerDemandBizService.actGetByCustomerTransactionId(transactionId).getD();
        if (customerDemandBean == null){
            logger.error(String.format(messageService.getMessage("MSG_CUSTOMERDEMAND_NOTFIND_TRANSACTIONID"),transactionId));
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CUSTOMERDEMAND_NOTFIND_TRANSACTIONID"),transactionId));
        }
        //  判断交易是否被拒绝
        CustomerTransactionBean customerTransactionBean = iCustomerTransactionBizService.actFindCustomerTransactionById(transactionId).getD();
        if (customerTransactionBean == null){
            logger.error(String.format(messageService.getMessage("TRANSACTION_NOTFOUN_ID"),transactionId));
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("TRANSACTION_NOTFOUN_ID"),transactionId));
        }
        if (customerTransactionBean.getStatus() == CustomerTransactionBean.TRANSACTION_STOP) {
            //  获取客户信息
            CustomerBean customer = iCustomerBizService.actGetCustomerById(customerDemandBean.getCustomerId()).getD();

            RejectCustomer rejectCustomer = new RejectCustomer();
            rejectCustomer.setIdentifyNo(customer.getIdentifyNo());
            rejectCustomer.setTransactionId(transactionId);
            rejectCustomer.setCustomerId(customerDemandBean.getCustomerId());
            //  判断被拒绝人的身份
            List<CustomerRelation> customerRelations = new ArrayList<CustomerRelation>();
            CustomerRelation  customerRelation;
            if (!StringUtils.isEmpty(customerDemandBean.getCustomerId())){
                customerRelation = new CustomerRelation();
                customerRelation.setCustomerId(customerDemandBean.getCustomerId());
                customerRelation.setRelation(CustomerRelation.RELATION_SELF);
                customerRelations.add(customerRelation);
            }
            if (!StringUtils.isEmpty(customerDemandBean.getMateCustomerId())){
                customerRelation = new CustomerRelation();
                customerRelation.setCustomerId(customerDemandBean.getMateCustomerId());
                customerRelation.setRelation(CustomerRelation.RELATION_MATE);
                customerRelations.add(customerRelation);
            }
            if (!StringUtils.isEmpty(customerDemandBean.getPledgeCustomerId())) {
                customerRelation = new CustomerRelation();
                customerRelation.setCustomerId(customerDemandBean.getPledgeCustomerId());
                customerRelation.setRelation(CustomerRelation.RELATION_PLEDGE);
                customerRelations.add(customerRelation);
            }
            rejectCustomer.setCustomerRelations(customerRelations);
            //  根据billTypeCode和交易ID，获取签批信息，并获取拒绝原因
            List<SignInfo>  signInfos = iWorkflowBizService.actGetSingInfosByFlowCodeAndTransactionId(billTypeCode, transactionId).getD();
            String reason = "";
            for (SignInfo signInfo : signInfos){
                if (signInfo.getResult() == 9){
                    reason += (signInfo.getComment() + ";");
                }
            }
            rejectCustomer.setRejectReason(reason);
            rejectCustomer.setApproveCustomerId(customerDemandBean.getApproveUserId());
            rejectCustomer.setApproveDate(customerDemandBean.getApproveDate());
            rejectCustomer = iRejectCustomerService.save(rejectCustomer);
            return ResultBean.getSucceed().setD(mappingService.map(rejectCustomer,RejectCustomerBean.class));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_REJECTCUSTOMER_TRANSACTION_NOT_REJECT"));
    }

    @Override
    public ResultBean<RejectCustomerBean> actSearchRejectCustomers(SearchBean searchBean){
        Page<RejectCustomer> rejectCustomerPage = iRejectCustomerService.findAllBySearchBean(searchBean);
        return ResultBean.getSucceed().setD(mappingService.map(rejectCustomerPage, RejectCustomerBean.class));
    }

}
