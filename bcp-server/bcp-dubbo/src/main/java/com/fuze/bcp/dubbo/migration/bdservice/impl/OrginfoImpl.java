package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Orginfo;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by ${Liu} on 2017/10/19.
 */
public class OrginfoImpl implements Orginfo {

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

        com.fuze.bcp.bd.domain.Orginfo org = (com.fuze.bcp.bd.domain.Orginfo) entity;

        //取值
        String code = sourceMap.getString("code");
        Integer dataStatus = sourceMap.getInt("dataStatus");
        Integer depth = sourceMap.getInt("depth");
        Boolean isVirtual = sourceMap.getBoolean("isVirtual");
        String leaderId = sourceMap.getString("leaderId");
        String name = sourceMap.getString("name");
        String path = sourceMap.getString("path");
        String ts = sourceMap.getString("ts");
        String parentId = sourceMap.getString("parentId");

        //其中一个字段分期产品Id,新版本无该字段不做同步.

        //存值
        org.setId(sourceMap.getString("_id"));
        org.setCode(code);
        org.setDataStatus(dataStatus);
        org.setDepth(depth);
        org.setVirtual(isVirtual);
        org.setLeaderId(leaderId);
        org.setName(name);
        org.setPath(path);
        org.setTs(ts);
        if(parentId == null || "".equals(parentId)){
            org.setParentId("0");
        }else {
            org.setParentId(parentId);
        }

        System.out.println("-----------OrgInfo-------------迁移完成-----------------------------");

        return null;
    }
}
