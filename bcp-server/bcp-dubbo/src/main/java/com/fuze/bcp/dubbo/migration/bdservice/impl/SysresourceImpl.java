package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.auth.domain.SysResource;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Sysresource;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

/**
 * Created by admin on 2017/10/18.
 */
public class SysresourceImpl implements Sysresource {

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

        SysResource sysresource = (SysResource) entity;

        String code = sourceMap.getString("code");
        String comment = sourceMap.getString("comment");
        Integer  dataStatus= sourceMap.getInt("dataStatus");
        String dataurl = sourceMap.getString("dataurl") ;
        Double depth = sourceMap.getDouble("depth");
        Boolean isVirtual = sourceMap.getBoolean("isVirtual");
        String name = sourceMap.getString("name");
        String operationCode = sourceMap.getString("operationCode");
        String operationName = sourceMap.getString("operationName");
        String parentId = sourceMap.getString("parentId");
        String path = sourceMap.getString("path");
        String ts = sourceMap.getString("ts") ;
        String url = sourceMap.getString("url");

        sysresource.setId(sourceMap.getString("_id"));
        sysresource.setCode(code);
        sysresource.setComment(comment);
        sysresource.setDataStatus(dataStatus);
        sysresource.setDataurl(dataurl);
        sysresource.setDepth(depth.intValue());
        sysresource.setIsVirtual(isVirtual);
        sysresource.setName(name);
        sysresource.setOperationCode(operationCode);
        sysresource.setOperationName(operationName);
        sysresource.setParentId(parentId == null || "".equals(parentId) ? "0":parentId);
        sysresource.setPath(path);
        sysresource.setTs(ts);
        sysresource.setUrl(url);

        System.out.println("-----------Syssource-------------迁移完成-----------------------------");

        return null;
    }
}
