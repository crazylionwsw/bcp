package com.fuze.bcp.dubbo.migration.service.impl;

import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.creditcar.domain.CarRegistry;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.dubbo.migration.DataMigration;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinglu on 2017/10/19.
 */
public class CarRegistryImpl implements DataMigration {
    @Override
    public Map<String, String> billDataMigration(CustomerDemand customerDemand, CustomerTransaction transaction, BasicDBObject sourceObj, BaseBillEntity targetObj, MongoTemplate source, MongoTemplate target) {
        Map map = new HashMap<>();
        CarRegistry carRegistry = (CarRegistry) targetObj;
        if (transaction.getBusinessTypeCode().equals("NC")) {
            carRegistry.setPickDate((String) sourceObj.get("pickDate"));//提车日期
            Query query = new Query();
            query.addCriteria(Criteria.where("pickupCarId").is(sourceObj.get("_id").toString()));
            BasicDBObject obj = source.findOne(query, BasicDBObject.class, "so_carregistry");
            if (obj != null) {
                carRegistry.setRegistryDate((String) obj.get("registryDate"));//上牌日期
                carRegistry.setLicenseNumber((String) obj.get("licenseNumber"));//车牌号码
                carRegistry.setVin((String) obj.get("vin"));
                carRegistry.setRegistryNumber((String) obj.get("motorRegistrationNumber"));//车辆登记证号码
                carRegistry.setMotorNumber((String) obj.get("engineNo"));//发动机号
                carRegistry.setCarModelNumber((String) obj.get("labelType"));//车辆型号 (国标型号)
                if (carRegistry.getRegistryDate() != null) {
                    carRegistry.setApproveStatus(ApproveStatus.APPROVE_PASSED);
                } else {
                    carRegistry.setApproveStatus(ApproveStatus.APPROVE_ONGOING);
                }
                map.put(DataMigration.SAVED, DataMigration.YES);
            }

        } else {
            map.put(DataMigration.SAVED, DataMigration.NO);
        }
        System.out.println("-----------------------------【CarRegistry】迁移完成-----------------------------");
        return map;
    }
}
