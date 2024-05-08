package com.fuze.bcp.transaction.business;

import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.TransactionStageBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.transaction.service.ICustomerTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/8/8.
 */
@Service
public class BizCustomerTransactionService implements ICustomerTransactionBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCustomerTransactionService.class);

    @Autowired
    ICustomerTransactionService iCustomerTransactionService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Override
    public ResultBean<List<CustomerTransactionBean>> actFindAllTransactions() {
        List<CustomerTransaction> all = iCustomerTransactionService.getAll();
        if (all.size() > 0 && all != null) {
            return ResultBean.getSucceed().setD(mappingService.map(all, CustomerTransactionBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerTransactionBean> actFindCustomerTransactionById(String id) {
        CustomerTransaction customerTransaction = iCustomerTransactionService.getOne(id);
        if (customerTransaction != null) {
            return ResultBean.getSucceed().setD(mappingService.map(customerTransaction, CustomerTransactionBean.class));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
    }

    @Override
    public ResultBean<CustomerTransactionBean> actFindAvailableCustomerTransactionById(String id) {
        CustomerTransaction customerTransaction = iCustomerTransactionService.getAvailableOne(id);
        if (customerTransaction != null) {
            return ResultBean.getSucceed().setD(mappingService.map(customerTransaction, CustomerTransactionBean.class));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
    }

    @Override
    public ResultBean<List<CustomerTransactionBean>> actGetTransactions(List<String> ids) {
        List<CustomerTransaction> all = iCustomerTransactionService.getAvaliableList(ids);
        if (all.size() > 0 && all != null) {
            return ResultBean.getSucceed().setD(mappingService.map(all, CustomerTransactionBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerTransactionBean> actSaveCustomerTransaction(CustomerTransactionBean customerTransactionBean) {
        CustomerTransaction customerTransaction = mappingService.map(customerTransactionBean, CustomerTransaction.class);
        if (customerTransaction != null) {
            customerTransaction = iCustomerTransactionService.save(customerTransaction);
            return ResultBean.getSucceed().setD(mappingService.map(customerTransaction, CustomerTransactionBean.class));
        }
        return ResultBean.getFailed();
    }

    public ResultBean<TransactionStageBean> actGetTransactionStage(String tid) {

        CustomerTransaction ct = iCustomerTransactionService.getOne(tid);
        String branchCode = ct.getBusinessTypeCode();
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanByTransactionId(tid).getD();
        if (customerLoanBean != null && customerLoanBean.getIsNeedPayment() != null) {
            branchCode += customerLoanBean.getIsNeedPayment().toString();
        } else {
            branchCode += "0";
        }

        return ResultBean.getSucceed().setD(this.actGetTransactionStage(tid, branchCode, "CAR_BIZ_WORKFLOW_STAGE_WEB").getD());
    }

    public ResultBean<TransactionStageBean> actGetTransactionStage(String tid, String bizCode, String paramCode) {
        WorkFlowBillBean workFlowBill = iWorkflowBizService.actGetBillWorkflow(tid).getD();
        if (workFlowBill == null){
            logger.error(String.format(messageService.getMessage("MSG_WORKFLOWBILL_NULL_TID"),tid));
            return null;
        }

        List<String> taskKeys = workFlowBill.getCurrentTasks();
        TransactionStageBean transactionStage = new TransactionStageBean();

        //获取系统业务阶段配置
        Map<?, ?> param = iParamBizService.actGetMap(paramCode).getD();
        List<Map<?, ?>> stages = (List<Map<?, ?>>) param.get(bizCode);

        List<Map<String, String>> definedStages = new ArrayList<>();
        for (Map<?, ?> stage : stages) {
            Map<String, String> stageKey = new HashMap<String, String>();
            stageKey.put("code", (String) stage.get("code"));
            stageKey.put("name", (String) stage.get("name"));
            definedStages.add(stageKey);
        }

        if (taskKeys == null || taskKeys.size() == 0) {
            if (workFlowBill.getCompletedTask() == null) { //刚开始业务
                transactionStage.setCurrentStage((String) stages.get(0).get("code")); //设置当前阶段为第一个阶段
            } else {
                if (workFlowBill.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { //业务已完成
                    transactionStage.setCurrentStage((String) stages.get(stages.size() - 1).get("code")); //设置为最后一个阶段
                }
                if (workFlowBill.getApproveStatus() == ApproveStatus.APPROVE_REJECT) { //业务被拒绝
                    transactionStage.setCurrentStage(workFlowBill.getCompletedTask());
                }
            }
        } else {
            for (Map<?, ?> stage : stages) {
                List<String> containKeys = (List<String>) stage.get("data");
                containKeys.retainAll(taskKeys);
                if (containKeys.size() > 0) {
                    transactionStage.setCurrentStage((String) stage.get("code"));
                    break;
                }
            }
        }
        transactionStage.setDefinedStages(definedStages);

        return ResultBean.getSucceed().setD(transactionStage);
    }


    @Override
    public ResultBean<CustomerTransactionBean> actSearchCustomerTransactions(SearchBean searchBean) {
        Page<CustomerTransaction> customerTransactions = iCustomerTransactionService.findAllPagesBySearchBean(searchBean);
        return ResultBean.getSucceed().setD(mappingService.map(customerTransactions, CustomerTransactionBean.class));
    }

    @Override
    public ResultBean<Integer> actCountTransactionsByCustomerId(String customerId) {
        Integer count = iCustomerTransactionService.countTransactionsByCustomerId(customerId);
        return ResultBean.getSucceed().setD(count);
    }

    @Override
    public ResultBean<Integer> actCountTransactionByCardealerId(String cardealerId) {
        List<String> list = new ArrayList<String>();
        list.add(cardealerId);
        List<CustomerTransaction> count = iCustomerTransactionService.getListsBySomeConditions(null, null, new ArrayList<String>(), new ArrayList<String>(), list, new ArrayList<Integer>(), "ts", true);
        return ResultBean.getSucceed().setD(count.size());
    }

    public ResultBean<List<CustomerTransactionBean>> actGetTransactionByCarDealerIds(List<String> ids) {
        List<CustomerTransaction> customertransactions = iCustomerTransactionService.getListsBySomeConditions(null, null, new ArrayList<String>(), new ArrayList<String>(), ids, new ArrayList<Integer>(), "ts", true);
        return ResultBean.getSucceed().setD(mappingService.map(customertransactions,CustomerTransactionBean.class));
    }

    public ResultBean<DataPageBean<CustomerTransactionBean>> actGetPagesBySomeConditions(String loginUserId, String selectTime, List<String> customerIds, List<String> employeeIds, List<String> carDealerIds, List<Integer> statusList, Integer pageIndex, Integer pageSize, String sortName, Boolean sortDesc){
        Page<CustomerTransaction> customerTransactions = iCustomerTransactionService.getPagesBySomeConditions(loginUserId, selectTime, customerIds, employeeIds, carDealerIds, statusList, pageIndex, pageSize, sortName, sortDesc);
        DataPageBean<CustomerTransactionBean> destination = new DataPageBean<CustomerTransactionBean>();
        destination.setPageSize(customerTransactions.getSize());
        destination.setTotalCount(customerTransactions.getTotalElements());
        destination.setTotalPages(customerTransactions.getTotalPages());
        destination.setCurrentPage(customerTransactions.getNumber());
        destination.setResult(mappingService.map(customerTransactions.getContent(),CustomerTransactionBean.class));
        return ResultBean.getSucceed().setD(destination);
    }

    public ResultBean<List<CustomerTransactionBean>> actGetListsBySomeConditions(String loginUserId, String selectTime, List<String> customerIds, List<String> employeeIds, List<String> carDealerIds, List<Integer> statusList, String sortName, Boolean sortDesc){
        List<CustomerTransaction> customerTransactions = iCustomerTransactionService.getListsBySomeConditions(loginUserId, selectTime, customerIds, employeeIds, carDealerIds, statusList, sortName, sortDesc);
        return ResultBean.getSucceed().setD(mappingService.map(customerTransactions,CustomerTransactionBean.class));
    }

    @Override
    public ResultBean<String> actCreateCustomerNumber(String ts, String businessTypeCode) {
        return ResultBean.getSucceed().setD(iCustomerTransactionService.createCustomerNumber(ts,businessTypeCode));
    }

    @Override
    public ResultBean<List<CustomerTransactionBean>> actGetAllBySearchBean(SearchBean searchBean) {
        List<CustomerTransaction> customerTransactions = iCustomerTransactionService.getAllBySearchBean(searchBean);
        return ResultBean.getSucceed().setD(mappingService.map(customerTransactions,CustomerTransactionBean.class));
    }

    public ResultBean<CustomerTransactionBean> actGetEditableTransaction(String tid){
        ResultBean resultBean = iCustomerTransactionService.getEditableTransaction(tid);
        if (resultBean.isSucceed()){
            return ResultBean.getSucceed().setD(mappingService.map(resultBean.getD(),CustomerTransactionBean.class));
        }
        return ResultBean.getFailed().setM(resultBean.getM());
    }

    @Override
    public ResultBean<List<String>> actGetTransactionIds(String loginUserId, Boolean isPass) {
        return ResultBean.getSucceed().setD(iCustomerTransactionService.getTransactionIds(loginUserId,isPass));
    }

    public ResultBean<List<String>> actGetAllUnFinishTransactionIds(){
        return ResultBean.getSucceed().setD(iCustomerTransactionService.getAllUnFinishTransactionIds());
    }

    public ResultBean<List<String>> actGetTransactionIdsOnDecompress(String userId, Boolean isPass){
        return ResultBean.getSucceed().setD(iCustomerTransactionService.getTransactionIdsOnDecompress(userId, isPass));
    }
}
