package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.bd.domain.CarBrand;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.CarModel;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by CJ on 2017/11/19.
 */
public class CarModelImpl implements CarModel {
    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.bd.domain.CarModel carModel = (com.fuze.bcp.bd.domain.CarModel) entity;
        carModel.setId(sourceMap.getString("_id"));
        carModel.setName(sourceMap.getString("name"));
        carModel.setTs(sourceMap.getString("ts"));
        carModel.setDataStatus(sourceMap.getInt("dataStatus"));
        carModel.setGroupName(sourceMap.getString("groupName"));
        carModel.setRefModelId(sourceMap.getString("refModelId"));
        com.mongodb.DBRef dbRef = (com.mongodb.DBRef) sourceMap.get("carBrand");
        if (dbRef != null){
            BasicDBObject b = source.findOne(new Query(Criteria.where("_id").is(new ObjectId(dbRef.getId().toString()))), BasicDBObject.class, dbRef.getCollectionName());
            carModel.setCarBrandId(b.getString("_id"));
            carModel.setFullName(b.getString("name")+" "+carModel.getName());
        } else {
            carModel.setFullName(carModel.getName());
        }

        return null;
    }
}
