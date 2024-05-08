package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.bd.domain.CustomerImageType;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.GuaranteeWay;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * Created by CJ on 2017/10/21.
 */
public class GuaranteeWayImpl implements GuaranteeWay {
    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.bd.domain.GuaranteeWay obj = (com.fuze.bcp.bd.domain.GuaranteeWay) entity;
        obj.setId(sourceMap.getString("_id"));
        obj.setCode(sourceMap.getString("code"));
        obj.setName(sourceMap.getString("name"));
        obj.setDataStatus(sourceMap.getInt("dataStatus"));
        obj.setTs(sourceMap.getString("ts"));
        obj.setCustomerImageTypeIds((List<String>) sourceMap.get("customerImageTypeIds"));
        obj.setCustomerImageTypeList((List<CustomerImageType>) sourceMap.get("customerImageTypeList"));
        obj.setRequiredGuaranteeLetter(sourceMap.getBoolean("requiredGuaranteeLetter"));
        obj.setRequiredPledgeFile(sourceMap.getBoolean("requiredGuaranteeLetter"));

        System.out.println("-----------GuaranteeWay-------------迁移完成-----------------------------");

        return null;
    }
}
