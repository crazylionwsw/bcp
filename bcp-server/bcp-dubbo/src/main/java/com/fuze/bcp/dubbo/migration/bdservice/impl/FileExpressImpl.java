package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.FileExpress;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Liu} on 2017/10/19.
 */
public class FileExpressImpl implements FileExpress{

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

        com.fuze.bcp.creditcar.domain.FileExpress fe = (com.fuze.bcp.creditcar.domain.FileExpress) entity;

        //取值
        String businessTime = sourceMap.getString("businessTime");
        String carDealerId = sourceMap.getString("carDealerId");
        String cashSourceId = sourceMap.getString("cashSourceId");
        String code = sourceMap.getString("code");
        String customerId = sourceMap.getString("customerId");
        Integer dataStatus = sourceMap.getInt("dataStatus");
        String employeeId = sourceMap.getString("employeeId");
        String expressFirm = sourceMap.getString("expressFirm");
        String loginUserId = sourceMap.getString("loginUserId");
        String orginfoId = sourceMap.getString("orginfoId");
        String receiveAddress = sourceMap.getString("receiveAddress");
        String receiveTime = sourceMap.getString("receiveTime");
        String sendAddress = sourceMap.getString("sendAddress");
        String sendManId = sourceMap.getString("sendManId");
        String sendTime = sourceMap.getString("sendTime");
        Integer status = sourceMap.getInt("status");
        String ts = sourceMap.getString("ts");
        com.mongodb.DBRef billType = (com.mongodb.DBRef) sourceMap.get("billType");
        BasicDBObject billObj = source.findOne(new Query(Criteria.where("_id").is(new ObjectId(billType.getId().toString()))), BasicDBObject.class, billType.getCollectionName());

        //billtypes(循环)
        List<String> sendCustomerImageTypeIds = sourceMap.get("sendCustomerImageTypeIds") != null ? (List) sourceMap.get("sendCustomerImageTypeIds") : null;
        List sendImgCode = new ArrayList<>();
        for (String imgTypeId:sendCustomerImageTypeIds) {
            BasicDBObject img = source.findOne(new Query(Criteria.where("_id").is(new ObjectId(imgTypeId))), BasicDBObject.class, "bd_customerimagetype");
            sendImgCode.add(img.get("code").toString());
        }

        //存值
        fe.setId(sourceMap.getString("_id"));
        fe.setCarDealerId(carDealerId);
        fe.setCashSourceId(cashSourceId);
        fe.setCode(code);
        fe.setCustomerId(customerId);
        fe.setDataStatus(dataStatus);
        fe.setEmployeeId(employeeId);
        fe.setExpressFirm(expressFirm);
        fe.setLoginUserId(loginUserId);
        fe.setOrginfoId(orginfoId);
        fe.setReceiveAddress(receiveAddress);
        fe.setReceiveTime(receiveTime);
        fe.setSendAddress(sendAddress);
        fe.setSendManId(sendManId);
        fe.setSendTime(sendTime);
        fe.setTs(ts);
        fe.setBusinessTypeCode(billObj.get("code").toString());
        fe.setSendCustomerImageTypeCodes(sendImgCode);

        System.out.println("-----------FileExpress-------------迁移完成-----------------------------");

        return null;
    }
}
