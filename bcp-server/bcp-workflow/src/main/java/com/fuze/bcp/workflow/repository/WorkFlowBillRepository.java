package com.fuze.bcp.workflow.repository;

import com.fuze.bcp.repository.BaseDataRepository;
import com.fuze.bcp.workflow.domain.WorkFlowBill;

import java.util.List;

/**
 * Created by CJ on 2017/7/31.
 */
public interface WorkFlowBillRepository extends BaseDataRepository<WorkFlowBill, String> {

    WorkFlowBill findOneByDataStatusAndSourceIdAndFlowCode(Integer save, String billId, String billTypeCode);

    WorkFlowBill findOneByDataStatusAndSourceId(Integer save, String billId);

    List<WorkFlowBill> findByDataStatusAndTransactionIdOrderByTs(Integer ds, String tid);

    WorkFlowBill findOneByDataStatusAndTransactionIdAndFlowCode(Integer save, String transactionId, String billTypeCode);
}
