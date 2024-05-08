package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.bd.domain.CashSourceEmployee;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.CashsourcEemployee;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Liu} on 2017/10/20.
 */
public class CashsourcEemployeeImpl implements CashsourcEemployee{

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

        CashSourceEmployee cashSourceEmployee = (CashSourceEmployee) entity;

        //取值

        String cashSourceId = sourceMap.getString("cashSourceId");
        String name = sourceMap.getString("name");
        Integer gender = sourceMap.getInt("gender");
        String ts = sourceMap.getString("ts");
        Integer dataStatus = sourceMap.getInt("dataStatus");

        List cells = sourceMap.get("cell") != null ? (List) sourceMap.get("cell") : null;
        StringBuilder cer = new StringBuilder();
        if(cells != null && cells.size() > 0){
            cer.append(cells.get(0));
        }
        List emails = sourceMap.get("email") != null ? (List) sourceMap.get("email") : null;
        StringBuilder emr = new StringBuilder();
        if(emails != null){
            emr.append(emails.get(0));
        }

        //存值
        cashSourceEmployee.setId(sourceMap.getString("_id"));
        cashSourceEmployee.setCashSourceId(cashSourceId);
        cashSourceEmployee.setUsername(name);
        cashSourceEmployee.setGender(gender);
        cashSourceEmployee.setTs(ts);
        cashSourceEmployee.setDataStatus(dataStatus);
        cashSourceEmployee.setCell(cer.toString());
        cashSourceEmployee.setEmail(emr.toString());

        System.out.println("-------CashsourceEmployee---------迁移完成-----------------------");

        return null;
    }
}
