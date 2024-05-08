package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Employee;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * Created by CJ on 2017/10/21.
 */
public class EmployeeImpl implements Employee {
    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.bd.domain.Employee obj = (com.fuze.bcp.bd.domain.Employee) entity;
        obj.setId(sourceMap.getString("_id"));
        obj.setCode(sourceMap.getString("code"));
        obj.setName(sourceMap.getString("name"));
        obj.setDataStatus(sourceMap.getInt("dataStatus"));
        obj.setTs(sourceMap.getString("ts"));
        obj.setIdentifyNo(sourceMap.getString("identifyNo"));
        obj.setAvatarFileId(sourceMap.getString("avatarFileId"));
        obj.setBirthday(sourceMap.getString("birthday"));
        obj.setCell(sourceMap.getString("cell"));
        obj.setEmail(sourceMap.getString("email"));
        obj.setEmployeeNo(sourceMap.getString("employeeNo"));
        obj.setEmployeeRoles((List<String>) sourceMap.get("employeeRoles"));
        obj.setGender(sourceMap.getInt("gender"));
        obj.setLoginUserId(sourceMap.getString("loginUserId"));
        obj.setOrgInfoId(sourceMap.getString("orgInfoId"));
        obj.setUsername(sourceMap.getString("username"));
        System.out.println("-----------Employee-------------迁移完成-----------------------------");
        return null;
    }
}
