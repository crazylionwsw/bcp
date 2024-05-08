package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.bd.domain.CustomerImageType;
import com.fuze.bcp.bd.service.ICustomerImageTypeService;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.BillType;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Liu} on 2017/11/15.
 */
public class BillTypeImpl implements BillType{


    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

        com.fuze.bcp.bd.domain.BillType billType = (com.fuze.bcp.bd.domain.BillType) entity;

        String name = sourceMap.getString("name");
        String ts = sourceMap.getString("ts");
        String code = sourceMap.getString("code");
        Integer  dataStatus= sourceMap.getInt("dataStatus");
        String activitiFlowID =sourceMap.getString("activitiFlowID");

        List<String> cList = new ArrayList<String>();
        List<String> contractIds = sourceMap.get("contractIds") != null ? (List) sourceMap.get("contractIds") : null;
        if(contractIds != null && !contractIds.isEmpty()){
            cList.addAll(contractIds);
        }

        List<String> suggestedImageTypeCodes = new ArrayList<String>();
        List<String> suggestedImageTypeIds = sourceMap.get("suggestedImageTypeIds") != null ? (List) sourceMap.get("suggestedImageTypeIds") : null;
        if(suggestedImageTypeIds != null && !suggestedImageTypeIds.isEmpty()){
            for (String sid:suggestedImageTypeIds) {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(new ObjectId(sid)));
                BasicDBObject imageType = source.findOne(query, BasicDBObject.class, "bd_customerimagetype");
                if(imageType != null){
                    suggestedImageTypeCodes.add(imageType.getString("code"));
                }
            }
        }

        List<String> requiredImageTypeCodes = new ArrayList<String>();
        List<String> requiredImageTypeIds = sourceMap.get("requiredImageTypeIds") != null ? (List) sourceMap.get("requiredImageTypeIds") : null;
        if(requiredImageTypeIds != null && !requiredImageTypeIds.isEmpty()){
            for (String rid:requiredImageTypeIds) {
                Query query1 = new Query();
                query1.addCriteria(Criteria.where("_id").is(new ObjectId(rid)));
                BasicDBObject imageType1 = source.findOne(query1, BasicDBObject.class, "bd_customerimagetype");
                if(imageType1 != null){
                    requiredImageTypeCodes.add(imageType1.getString("code"));
                }
            }
        }

        billType.setId(sourceMap.getString("_id"));
        billType.setName(name);
        billType.setCode(code);
        billType.setTs(ts);
        billType.setDataStatus(dataStatus);
        billType.setActivitiFlowID(activitiFlowID);
        billType.setSuggestedImageTypeCodes(suggestedImageTypeCodes);
        billType.setRequiredImageTypeCodes(requiredImageTypeCodes);
        billType.setDocumentIds(cList);

        System.out.println("-----------BillType-------------迁移完成-----------------------------");

        return null;
    }
}
