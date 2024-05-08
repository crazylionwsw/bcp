package com.fuze.bcp.dubbo.migration.service.impl;

import com.fuze.bcp.api.creditcar.bean.CardActionRecord;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.creditcar.domain.DMVPledge;
import com.fuze.bcp.creditcar.repository.DMVPledgeRepository;
import com.fuze.bcp.creditcar.service.impl.BaseBillServiceImpl;
import com.fuze.bcp.dubbo.migration.DataMigration;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.workflow.domain.WorkFlowBill;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/10/19.
 */
public class DmvpledgeImpl extends BaseBillServiceImpl<DMVPledge, DMVPledgeRepository> implements DataMigration {

    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public Map<String, String> billDataMigration(CustomerDemand customerDemand, CustomerTransaction transaction, BasicDBObject sourceObj, BaseBillEntity targetObj, MongoTemplate source, MongoTemplate target) {
        DMVPledge dmvPledge = (DMVPledge) targetObj;
        dmvPledge.setStatus(sourceObj.get("status") != null ? new Double(sourceObj.get("status").toString()).intValue() : null);
        dmvPledge.setPledgeCustomerId(sourceObj.get("pledgeCustomerId") != null ? (String)sourceObj.get("pledgeCustomerId") : null);
        dmvPledge.setPledgeDateReceiveTime(sourceObj.get("pledgeDateReceiveTime") != null ? (String)sourceObj.get("pledgeDateReceiveTime") : null);
        dmvPledge.setContractStartTime(sourceObj.get("contractStartTime") != null ? (String)sourceObj.get("contractStartTime") : null);
        dmvPledge.setTakeContractTime(sourceObj.get("takeContractTime") != null ? (String)sourceObj.get("takeContractTime") : null);
        dmvPledge.setPledgeStartTime(sourceObj.get("pledgeStartTime") != null ? (String)sourceObj.get("pledgeStartTime") : null);
        dmvPledge.setPledgeEndTime(sourceObj.get("pledgeEndTime") != null ? (String)sourceObj.get("pledgeEndTime") : null);
        dmvPledge.setActionRecords(sourceObj.get("actionRecords") != null ? (List<CardActionRecord>)sourceObj.get("actionRecords") : null);
        dmvPledge.setApproveStatus(sourceObj.getInt("approveStatus"));
        if (dmvPledge.getPledgeEndTime() != null && !"".equals(dmvPledge.getPledgeEndTime())) {
            transaction.setStatus(CustomerTransactionBean.TRANSACTION_FINISH);
            target.save(transaction);
            WorkFlowBill w = target.findOne(new Query(Criteria.where("sourceId").is(transaction.getId())), WorkFlowBill.class);
            w.setCompletedTask(dmvPledge.getBillTypeCode());
            w.setCurrentTask(null);
            w.setCurrentTasks(new ArrayList<>());
            w.setApproveStatus(ApproveStatus.APPROVE_PASSED);
            target.save(w);
        }
        Map map = new HashMap<>();
        map.put(DataMigration.SAVED, DataMigration.YES);
        System.out.println("-----------------------------【DMVPledge】迁移完成-----------------------------");
        return map;
    }




}
