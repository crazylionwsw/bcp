package com.fuze.bcp.workflow.service;

import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.IBaseDataService;
import com.fuze.bcp.workflow.domain.WorkFlowBill;

import java.util.List;

/**
 * Created by CJ on 2017/7/31.
 */
public interface IWorkFlowBillService extends IBaseDataService<WorkFlowBill> {

    WorkFlowBill getOneByBillIdAndBillTypeCode(String billId, String billTypeCode);

    WorkFlowBill getOneByTransactionIdAndBillTypeCode(String transactionId, String billTypeCode);

    WorkFlowBill getOneByBillId(String billId);

    List<WorkFlowBill> getAllTransactionFlow(String tid);

    String stophBill(WorkFlowBill workflowBill, String userId) throws Exception;

    WorkFlowBill getWorkflowBillByBillIdAndFlowCode(String billId);

}
