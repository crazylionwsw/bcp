package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Loginuser;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/10/18.
 */
public class LoginuserImpl implements Loginuser {

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

        com.fuze.bcp.auth.domain.LoginUser user = (com.fuze.bcp.auth.domain.LoginUser) entity;

        String activitiUserId = sourceMap.getString("activitiUserId");
        Integer  dataStatus= sourceMap.getInt("dataStatus");
        Integer deviceNum = sourceMap.getInt("deviceNum");
        String password = sourceMap.getString("password");
        Boolean system = sourceMap.getBoolean("system");
        String ts = sourceMap.getString("ts");
        String username = sourceMap.getString("username");
        //集合
        List userRoleIds = sourceMap.get("userRoleIds") != null ? (List) sourceMap.get("userRoleIds") : null;
        List activitiUserRoles = sourceMap.get("activitiUserRoles") != null ? (List) sourceMap.get("activitiUserRoles") : null;


        List actRoleList = new ArrayList();
        if(activitiUserRoles != null){
            for (String str :(List<String>) activitiUserRoles) {
                if(str.equals("4")){
                    str = "G_APPROVAL";
                }else if(str.equals("5")){
                    str = "G_REVIEW";
                }else if(str.equals("6")){
                    str = "G_SUBMIT";
                }else if(str.equals("7")){
                    str = "G_SIGN";
                }else if(str.equals("8")){
                    str = "G_MANAGER";
                }else if(str.equals("9")){
                    str = "G_FINANCE";
                }
                actRoleList.add(str);
            }
        }

        List roleList = new ArrayList();
        if(userRoleIds != null){
            for (String uRole : (List<String>)userRoleIds) {
                roleList.add(uRole);
            }
        }

        user.setId(sourceMap.getString("_id"));
        user.setActivitiUserId(activitiUserId);
        user.setDataStatus(dataStatus);
        user.setDeviceNum(deviceNum);
        user.setPassword(password);
        user.setSystem(system);
        user.setTs(ts);
        user.setUsername(username);
        user.setActivitiUserRoles(actRoleList);
        user.setUserRoleIds(roleList);

        System.out.println("-----------LoginUser-------------迁移完成-----------------------------");
        return null;
    }
}
