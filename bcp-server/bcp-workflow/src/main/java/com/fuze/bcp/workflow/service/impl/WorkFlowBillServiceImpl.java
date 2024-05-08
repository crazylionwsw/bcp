package com.fuze.bcp.workflow.service.impl;

import com.fuze.bcp.api.workflow.bean.SignCondition;
import com.fuze.bcp.api.workflow.bean.TEMSignInfo;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.workflow.domain.WorkFlowBill;
import com.fuze.bcp.workflow.repository.WorkFlowBillRepository;
import com.fuze.bcp.workflow.service.IWorkFlowBillService;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/7/31.
 */
@Service
public class WorkFlowBillServiceImpl extends BaseDataServiceImpl<WorkFlowBill, WorkFlowBillRepository> implements IWorkFlowBillService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public WorkFlowBill getOneByBillIdAndBillTypeCode(String billId, String billTypeCode) {
        return super.baseRepository.findOneByDataStatusAndSourceIdAndFlowCode(DataStatus.SAVE, billId, billTypeCode);
    }

    @Override
    public WorkFlowBill getOneByTransactionIdAndBillTypeCode(String transactionId, String billTypeCode) {
        return super.baseRepository.findOneByDataStatusAndTransactionIdAndFlowCode(DataStatus.SAVE, transactionId, billTypeCode);
    }

    @Override
    public WorkFlowBill getOneByBillId(String billId) {
        return super.baseRepository.findOneByDataStatusAndSourceId(DataStatus.SAVE, billId);
    }

    public List<WorkFlowBill> getAllTransactionFlow(String tid) {
        return baseRepository.findByDataStatusAndTransactionIdOrderByTs(DataStatus.SAVE, tid);
    }

    @Override
    public String stophBill(WorkFlowBill workflowBill, String userId) throws Exception {
        String collectionName = workflowBill.getCollectionName();
        String id = workflowBill.getSourceId();
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        Update update = Update.update("dataStatus", DataStatus.SAVE).set("approveDate", DateTimeUtils.getCreateTime())
                .set("approveUserId", userId);
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, collectionName);
        if (!writeResult.isUpdateOfExisting()) {
            throw new Exception("修改单据状态失败,ID:" + id + ",集合:" + collectionName);
        }
        return id;

    }

    @Override
    public WorkFlowBill getWorkflowBillByBillIdAndFlowCode(String billId) {
        Query query = new Query();
        Criteria c = new Criteria();
        c.and("sourceId").is(billId);
        query.addCriteria(c);
        BasicDBObject obj = mongoTemplate.findOne(query, BasicDBObject.class, "wf_workflowbill");
        if (obj == null) {
            return null;
        }
        WorkFlowBill<TEMSignInfo> workFlowBill = new WorkFlowBill();
        workFlowBill.setApproveStatus(obj.containsField("approveStatus") ? obj.getInt("approveStatus") : null);
        workFlowBill.setDataStatus(obj.containsField("dataStatus") ? obj.getInt("dataStatus") : null);
        workFlowBill.setId(obj.getString("_id"));
        workFlowBill.setFlowCode(obj.getString("flowCode"));
        workFlowBill.setActivitiId(obj.getString("activitiId"));
        workFlowBill.setCurrentTask(obj.getString("currentTask"));
        workFlowBill.setBusinessTypeCode(obj.getString("businessTypeCode"));
        workFlowBill.setCollectionName(obj.getString("collectionName"));
        workFlowBill.setCompletedTask(obj.getString("completedTask"));
        List datas = new ArrayList<>();
        datas.addAll((List<String>) obj.get("currentTasks"));
        workFlowBill.setCurrentTasks(datas);
        workFlowBill.setTransactionId(obj.getString("transactionId"));
        workFlowBill.setSourceId(obj.getString("sourceId"));
        workFlowBill.setDoneTime(obj.getString("doneTime"));
        Map<String, List<BasicDBObject>> map = (Map<String, List<BasicDBObject>>) obj.get("params");
        Map<String, List<SignCondition>> signConditionMap = new HashMap<String, List<SignCondition>>();
        if(map != null){
            for (String key : map.keySet()) {
                List<BasicDBObject> list = map.get(key);
                List<SignCondition> signConditions = new ArrayList<>();
                for (BasicDBObject object : list) {
                    SignCondition signCondition = new SignCondition();
                    signCondition.setCollection(object.getString("collection"));
                    signCondition.setField(object.getString("field"));
                    signCondition.setDefaultValue(object.getString("defaultValue"));
                    signCondition.setLabel(object.getString("label"));
                    signCondition.setValue(object.getString("value"));
                    signCondition.setRealValue(object.getBoolean("realValue"));
                    signCondition.setCondition((Map<String, Object>) object.get("condition"));
                    signConditions.add(signCondition);
                }
                signConditionMap.put(key, signConditions);
            }
        }
        workFlowBill.setParams(signConditionMap);
        List<BasicDBObject> signLists = (List<BasicDBObject>) obj.get("signInfos");
        List<TEMSignInfo> signInfos = new ArrayList<>();
        for (BasicDBObject object : signLists) {
            TEMSignInfo signInfo = new TEMSignInfo();
            signInfo.setAuditRole(object.getString("auditRole"));
            Map vars = new HashMap<>();
            if (object.get("approveVars") != null) {
                vars.putAll((Map) object.get("approveVars"));
            }
            signInfo.setApproveVars(vars);
            signInfo.setAuditStatus(object.containsField("auditStatus") ? object.getInt("auditStatus") : null);
            signInfo.setCreditAmount(object.containsField("creditAmount") ? object.getDouble("creditAmount") : null);
            signInfo.setTaskInfo(object.getString("taskInfo"));
            signInfo.setComment(object.getString("comment"));
            signInfo.setDownPaymentRatio(object.containsField("downPaymentRatio") ? object.getDouble("downPaymentRatio") : null);
            signInfo.setEmployeeId(object.getString("employeeId"));
            signInfo.setFromSalesman(object.getBoolean("fromSalesman"));
            signInfo.setFlag(object.containsField("flag") ? object.getInt("flag") : null);
            List datas2 = new ArrayList<>();
            datas2.addAll((List<String>) object.get("noteTemplateIds"));
            signInfo.setNoteTemplateIds((datas2));
            signInfo.setResult(object.containsField("result") ? object.getInt("result") : null);
            signInfo.setTs(object.getString("ts"));
            signInfo.setUserId(object.getString("userId"));
            signInfos.add(signInfo);
        }
        workFlowBill.setSignInfos(signInfos);
        return workFlowBill;
    }

}
