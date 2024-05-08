package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Province;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by CJ on 2017/10/21.
 */
public class ProvinceImpl implements Province {

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.bd.domain.Province obj = (com.fuze.bcp.bd.domain.Province) entity;
        obj.setId(sourceMap.getString("_id"));
        obj.setPath(sourceMap.getString("path"));
        obj.setDepth(sourceMap.getInt("depth"));
        obj.setParentId(sourceMap.getString("parentId"));
        obj.setCode(sourceMap.getString("code"));
        obj.setName(sourceMap.getString("name"));
        obj.setDataStatus(sourceMap.getInt("dataStatus"));
        obj.setTs(sourceMap.getString("ts"));

        System.out.println("-----------Province-------------迁移完成-----------------------------");

        return null;


    }
}
