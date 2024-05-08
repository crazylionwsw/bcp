package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.DeviceUsage;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by CJ on 2017/10/20.
 */
public class DeviceUsageImpl implements DeviceUsage {

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.sys.domain.DeviceUsage obj = (com.fuze.bcp.sys.domain.DeviceUsage) entity;
        obj.setId(sourceMap.getString("_id"));
        obj.setDataStatus(sourceMap.getInt("dataStatus"));
        obj.setTs(sourceMap.getString("ts"));
        obj.setUsageType(sourceMap.getInt("usageType"));
        obj.setDeviceId(sourceMap.getString("deviceId"));
        obj.setEmployeeId(sourceMap.getString("employeeId"));
        return null;


    }

}
