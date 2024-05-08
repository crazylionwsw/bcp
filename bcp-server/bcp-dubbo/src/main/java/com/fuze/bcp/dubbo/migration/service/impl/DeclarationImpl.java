package com.fuze.bcp.dubbo.migration.service.impl;

import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.dubbo.migration.DataMigration;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CJ on 2017/10/17.
 */
public class DeclarationImpl implements DataMigration {
    @Override
    public Map<String, String> billDataMigration(CustomerDemand customerDemand, CustomerTransaction transaction, BasicDBObject sourceObj, BaseBillEntity targetObj, MongoTemplate source, MongoTemplate target) {

        Map map = new HashMap<>();
        map.put(DataMigration.SAVED, DataMigration.YES);
        return map;
    }
}
