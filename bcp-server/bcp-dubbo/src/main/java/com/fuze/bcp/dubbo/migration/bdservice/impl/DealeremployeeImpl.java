package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Dealeremployee;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * Created by CJ on 2017/10/21.
 */
public class DealeremployeeImpl implements Dealeremployee {
    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.bd.domain.DealerEmployee obj = (com.fuze.bcp.bd.domain.DealerEmployee) entity;
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
        obj.setCarDealerId(sourceMap.getString("carDealerId"));
        obj.setCareer(sourceMap.getString("career"));
        obj.setRoles(sourceMap.getString("roles"));

        System.out.println("-----------DealerEmployee-------------迁移完成-----------------------------");
        return null;
    }
}
