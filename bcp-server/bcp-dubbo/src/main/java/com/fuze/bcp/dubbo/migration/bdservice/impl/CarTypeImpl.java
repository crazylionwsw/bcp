package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.bd.domain.CarModel;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.CarType;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.quartz.impl.calendar.BaseCalendar;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by CJ on 2017/11/19.
 */
public class CarTypeImpl implements CarType {
    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

//        "_id" : ObjectId("58fa06c077c82cd3aa0dce16"),
//                "motiveType" : 0,
//                "refStyleId" : "116685",
//                "ml" : "3.0T",
//                "colors" : [],
//        "price" : 1628000,
//                "groupName" : "2016 款",
//                "name" : "2016 款 3.0T 自动 V6 SC Vogue加长版",
//                "dataStatus" : 1,
//                "ts" : "2017-04-21 21:18:52",
//                "carBrand" : {
//            "$ref" : "bd_carbrand",
//                    "$id" : ObjectId("58fa051c77c8b44a492957a0")
//        },
//        "carModel" : {
//            "$ref" : "bd_carmodel",
//                    "$id" : ObjectId("58fa058477c8c09005b94aba")
//        }

        com.fuze.bcp.bd.domain.CarType carType = (com.fuze.bcp.bd.domain.CarType) entity;
        com.mongodb.DBRef dbRef1 = (com.mongodb.DBRef) sourceMap.get("carBrand");
        if (dbRef1 != null) {
            BasicDBObject carBrand = source.findOne(new Query(Criteria.where("_id").is(new ObjectId(dbRef1.getId().toString()))), BasicDBObject.class, dbRef1.getCollectionName());
            carType.setCarBrandId(carBrand.getString("_id"));
        }
        com.mongodb.DBRef dbRef2 = (com.mongodb.DBRef) sourceMap.get("carModel");
        if (dbRef2 != null) {
            CarModel carModel = target.findOne(new Query(Criteria.where("_id").is(new ObjectId(dbRef2.getId().toString()))), CarModel.class);
            carType.setCarModelId(carModel.getId());
            carType.setFullName(carModel.getFullName()+" "+sourceMap.getString("name"));
        }else {
            carType.setFullName(sourceMap.getString("name"));
        }
        carType.setColors((List<String>) sourceMap.get("colors"));
        carType.setDataStatus(sourceMap.getInt("dataStatus"));

        carType.setGroupName(sourceMap.getString("groupName"));
        carType.setId(sourceMap.getString("_id"));
        carType.setMl(sourceMap.getString("ml"));
        carType.setMotiveType(sourceMap.getInt("motiveType"));
        carType.setName(sourceMap.getString("name"));
        carType.setPrice(sourceMap.getDouble("price"));
        carType.setRefStyleId(sourceMap.getString("refStyleId"));
        carType.setTs(sourceMap.getString("ts"));
        System.out.println("-----------CarType-------------迁移完成-----------------------------");
        return null;
    }
}
