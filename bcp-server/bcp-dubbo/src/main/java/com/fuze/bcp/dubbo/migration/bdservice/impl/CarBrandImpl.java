package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.CarBrand;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by CJ on 2017/11/19.
 */
public class CarBrandImpl implements CarBrand {
    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.bd.domain.CarBrand carBrand = (com.fuze.bcp.bd.domain.CarBrand) entity;
        carBrand.setId(sourceMap.getString("_id"));
        carBrand.setCode(sourceMap.getString("code"));
        carBrand.setName(sourceMap.getString("name"));
        carBrand.setGroupName(sourceMap.getString("groupName"));
        carBrand.setRefMakeId(sourceMap.getString("refMakeId"));
        carBrand.setDataStatus(sourceMap.getInt("dataStatus"));
        carBrand.setTs(sourceMap.getString("ts"));
        return null;
    }
}
