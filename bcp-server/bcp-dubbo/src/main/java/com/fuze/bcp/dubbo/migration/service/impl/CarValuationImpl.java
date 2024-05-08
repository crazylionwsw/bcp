package com.fuze.bcp.dubbo.migration.service.impl;

import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.creditcar.domain.CarValuation;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.customer.domain.CustomerCar;
import com.fuze.bcp.dubbo.migration.DataMigration;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinglu on 2017/10/19.
 */
public class CarValuationImpl implements DataMigration {

    @Override
    public Map<String, String> billDataMigration(CustomerDemand customerDemand, CustomerTransaction transaction, BasicDBObject sourceObj, BaseBillEntity targetObj, MongoTemplate source, MongoTemplate target) {
        Map map = new HashMap<>();
        PurchaseCarOrder order = target.findOne(new Query(Criteria.where("customerTransactionId").is(transaction.getId())), PurchaseCarOrder.class);
        CustomerCar customerCar = target.findOne(new Query(Criteria.where("_id").is(order.getCustomerCarId())), CustomerCar.class);
        CarValuation carValuation = (CarValuation) targetObj;
        //  生成二手车评估信息
        if ("OC".equals(order.getBusinessTypeCode())) {
            carValuation.setCarTypeId(customerCar.getCarTypeId());
            carValuation.setVin(customerCar.getVin());
            carValuation.setLicenceNumber(customerCar.getLicenseNumber());
            carValuation.setCarModelNumber(customerCar.getCarModelNumber());
            carValuation.setFirstRegistryDate(customerCar.getFirstRegistryDate());
            carValuation.setMileage(customerCar.getMileage());
            carValuation.setColor(customerCar.getCarColor());
            if (transaction.getStatus() != CustomerTransactionBean.TRANSACTION_STOP || transaction.getStatus() != CustomerTransactionBean.TRANSACTION_CANCELLED
                    || transaction.getStatus() != CustomerTransactionBean.TRANSACTION_FINISH) {
                if (order.getApproveStatus() == ApproveStatus.APPROVE_REJECT || order.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
                    carValuation.setFinishOrder(true);
                } else {
                    carValuation.setFinishOrder(false);
                }
            } else {
                carValuation.setFinishOrder(false);
            }
            carValuation.setQueryMaintenance(1);
            carValuation.setPrice(customerCar.getEvaluatePrice()); // 现场评估价
            carValuation.setOrderId(order.getId()); // 签约单ID
            target.save(carValuation);
            CustomerCar c1 = target.findOne(new Query(Criteria.where("_id").is(new ObjectId(customerDemand.getCustomerCarId()))), CustomerCar.class);
            c1.setEvaluatePrice(carValuation.getPrice());
            target.save(c1);
            order.setVehicleEvaluateInfoId(carValuation.getId());
            target.save(order);
            map.put(DataMigration.SAVED, DataMigration.YES);
        } else {
            map.put(DataMigration.SAVED, DataMigration.NO);
        }
        System.out.println("-----------------------------【CarValuation】迁移完成-----------------------------");
        return map;
    }
}
