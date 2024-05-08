package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Repaymentway;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

/**
 * Created by CJ on 2017/10/21.
 */
public class RepaymentwayImpl implements Repaymentway {
    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.bd.domain.RepaymentWay obj = (com.fuze.bcp.bd.domain.RepaymentWay) entity;
        obj.setId(sourceMap.getString("_id"));
        obj.setMonths(sourceMap.getInt("months"));
        obj.setCount(sourceMap.getInt("count"));
        obj.setRatioes((Map) sourceMap.get("ratioes"));
        obj.setCode(sourceMap.getString("code"));
        obj.setName(sourceMap.getString("name"));
        obj.setDataStatus(sourceMap.getInt("dataStatus"));
        obj.setTs(sourceMap.getString("ts"));

        System.out.println("-----------RepaymentWay-------------迁移完成-----------------------------");

        return null;
    }
}
