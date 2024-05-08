package com.fuze.bcp.dubbo.migration.service;

import com.alibaba.dubbo.common.json.JSONObject;
import com.fuze.bcp.bd.domain.Orginfo;
import com.fuze.bcp.dubbo.migration.mongo.MongoConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.List;

/**
 * Created by ${Liu} on 2018/2/6.
 */
public class BDTestMain {


    public static void main(String[] args) {
        MongoTemplate target = MongoConnect.getMongoTemplate("192.168.0.20", 27017, "mongodev", "admin", "FuzefenqiDev11", "bcp_dev");
        List<Orginfo> all = target.findAll(Orginfo.class);
        for (int i = 0; i < all.size() ; i++) {
            Orginfo orginfo = all.get(i);
            orginfo.setWcqyId(String.valueOf(i+2));
            target.save(orginfo,"bd_orginfo_wx");
        }
        System.out.println(all);
    }







}
