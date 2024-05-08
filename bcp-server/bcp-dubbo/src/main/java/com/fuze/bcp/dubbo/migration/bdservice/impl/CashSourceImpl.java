package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.CashSource;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * Created by CJ on 2017/10/23.
 */
public class CashSourceImpl implements CashSource {
    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.bd.domain.CashSource obj = (com.fuze.bcp.bd.domain.CashSource) entity;
        obj.setId(sourceMap.getString("_id"));
        obj.setCode(sourceMap.getString("code"));
        obj.setName(sourceMap.getString("name"));
        obj.setDataStatus(sourceMap.getInt("dataStatus"));
        obj.setTs(sourceMap.getString("ts"));
        obj.setAddress(sourceMap.getString("address"));
        obj.setCell(sourceMap.getString("cell"));
        obj.setContacts(sourceMap.getString("contacts"));
        obj.setMarketingCode((List<String>) sourceMap.get("marketingCode"));
        obj.setShortName(sourceMap.getString("shortName"));
        obj.setSourceType(sourceMap.getInt("sourceType"));
        obj.setDepth(sourceMap.getInt("depth"));
        obj.setPath(sourceMap.getString("path"));
        obj.setParentId(sourceMap.getString("parentId") == null || "".equals(sourceMap.getString("parentId")) ? "0" : sourceMap.getString("parentId"));
        System.out.println("-----------CashSource-------------迁移完成-----------------------------");

        return null;
    }
}
