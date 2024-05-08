package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.auth.domain.SysRole;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Loginuser;
import com.fuze.bcp.dubbo.migration.bdservice.Sysrole;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/10/18.
 */
public class SysroleImpl implements Sysrole {

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

        SysRole role = (SysRole) entity;

        Integer  dataStatus= sourceMap.getInt("dataStatus");
        String name = sourceMap.getString("name");
        String ts = sourceMap.getString("ts");
        //集合
        List sysResourceIds = sourceMap.get("sysResourceIds") != null ? (List) sourceMap.get("sysResourceIds") : null;

        List roleList = new ArrayList();
        if(roleList != null){
            for (String uRole : (List<String>)sysResourceIds) {
                roleList.add(uRole);
            }
        }

        role.setId(sourceMap.getString("_id"));
        role.setDataStatus(dataStatus);
        role.setName(name);
        role.setTs(ts);
        role.setSysResourceIds(roleList);
        //新数据库中多出code字段
        if(name.equals("分期经理")){
            role.setCode("SALES_MANAGER");
        }else if(name.equals("产品经理")){
            role.setCode("PRODUCT_MANAGER");
        }else if(name.equals("签批人")){
            role.setCode(null);
        }else if(name.equals("系统管理")){
            role.setCode("ADMINISTRATOR");
        }else if(name.equals("审批专员")){
            role.setCode("APPROVAL");
        }else if(name.equals("综合专员")){
            role.setCode("COMPOSITIVE_MANAGER");
        }else if(name.equals("人事专员")){
            role.setCode(null);
        }else if(name.equals("渠道经理")){
            role.setCode("CHANNEL_MANAGER");
        }else if(name.equals("部门经理")){
            role.setCode("DIVISION_MANAGER");
        }else if(name.equals("抵押专员")){
            role.setCode("PLEDGE_MANAGER");
        }else if(name.equals("放款专员")){
            role.setCode("LOAN_MANAGER");
        }else if(name.equals("财务经理")){
            role.setCode("FINANCIAL_MANAGER");
        }else if(name.equals("数据维护")){
            role.setCode(null);
        }else if(name.equals("基础数据维护")){
            role.setCode(null);
        }else if(name.equals("人事经理")){
            role.setCode("HR_MANAGER");
        }else if(name.equals("审查专员")){
            role.setCode("REVIEW");
        }

        System.out.println("-----------Sysrole-------------迁移完成-----------------------------");

        return null;
    }
}
