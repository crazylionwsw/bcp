package com.fuze.bcp.dubbo.migration.service.impl;

import com.fuze.bcp.creditcar.domain.CancelOrder;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.dubbo.migration.DataMigration;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinglu on 2017/11/21.
 */
public class CancelOrderImpl implements DataMigration {
    @Override
    public Map<String, String> billDataMigration(CustomerDemand customerDemand, CustomerTransaction transaction, BasicDBObject sourceObj, BaseBillEntity targetObj, MongoTemplate source, MongoTemplate target) throws IOException {
        CancelOrder cancelOrder = (CancelOrder) targetObj;
        cancelOrder.setReason(sourceObj.get("reason") != null ? (String) sourceObj.get("reason") : null);
        target.save(cancelOrder);
        System.out.println("-----------------------------【CancelOrder】迁移完成-----------------------------");
        Map map = new HashMap<>();
        map.put(DataMigration.SAVED, DataMigration.YES);
        return map;
    }
}
