package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.TerminalDevice;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by CJ on 2017/10/20.
 */
public class TerminalDeviceImpl implements TerminalDevice {
    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.sys.domain.TerminalDevice obj = (com.fuze.bcp.sys.domain.TerminalDevice) entity;
        obj.setId(sourceMap.getString("_id"));
        obj.setDataStatus(sourceMap.getInt("dataStatus"));
        obj.setTs(sourceMap.getString("ts"));
        obj.setDeviceName(sourceMap.getString("deviceName"));
        obj.setDeviceType(sourceMap.getString("deviceType"));
        obj.setEmployeeId(sourceMap.getString("employeeId"));
        obj.setIdentify(sourceMap.getString("identify"));
        obj.setMac(sourceMap.getString("mac"));
        obj.setSerialNum(sourceMap.getString("serialNum"));

        System.out.println("-----------Terminal-------------迁移完成-----------------------------");

        return null;
    }
}
