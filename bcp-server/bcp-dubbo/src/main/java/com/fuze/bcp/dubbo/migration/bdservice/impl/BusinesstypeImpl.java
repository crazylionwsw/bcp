package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.bd.domain.BusinessType;
import com.fuze.bcp.bean.BusinessRate;
import com.fuze.bcp.bean.RateType;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Businesstype;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/10/18.
 */
public class BusinesstypeImpl implements Businesstype {

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        BusinessType businessType = (BusinessType) entity;
        String name = sourceMap.getString("name");
        String ts = sourceMap.getString("ts");
        String code = sourceMap.getString("code");
        Integer  dataStatus= sourceMap.getInt("dataStatus");
        //billtypes(循环)
        List billTypes = sourceMap.get("billTypes") != null ? (List) sourceMap.get("billTypes") : null;
        List billTypeCodes = new ArrayList();
        for (Object b:billTypes) {
            com.mongodb.DBRef billTypeDBRef = (com.mongodb.DBRef) b;
            BasicDBObject billObj = source.findOne(new Query(Criteria.where("_id").is(new ObjectId(billTypeDBRef.getId().toString()))), BasicDBObject.class, billTypeDBRef.getCollectionName());
            billTypeCodes.add(billObj.getString("code"));
        }
        businessType.setId(sourceMap.getString("_id"));
        businessType.setName(name);
        businessType.setTs(ts);
        businessType.setCode(code);
        businessType.setDataStatus(dataStatus);
        businessType.setBillTypeCodes(billTypeCodes);
        if ("OC".equals(code)) {
            List<RateType> rateTypeList1 = new ArrayList<>();
            RateType t1 = new RateType();
            t1.setMonths(12);
            t1.setRatio(0.05);
            RateType t2 = new RateType();
            t2.setMonths(18);
            t2.setRatio(0.075);
            RateType t3 = new RateType();
            t3.setMonths(24);
            t3.setRatio(0.10);
            RateType t5 = new RateType();
            t5.setMonths(36);
            t5.setRatio(0.15);
            rateTypeList1.add(t1);
            rateTypeList1.add(t2);
            rateTypeList1.add(t3);
            rateTypeList1.add(t5);
            BusinessRate businessRate = new BusinessRate();
            businessRate.setSourceRateId(BusinessRate.DEF_SOURCE_RATE_ID_2);
            businessRate.setRateTypeList(rateTypeList1);
            List<BusinessRate> _rateTypeList = new ArrayList<BusinessRate>();
            _rateTypeList.add(businessRate);
            businessType.setRateTypes(_rateTypeList);
        } else if ("NC".equals(code)) {
            List<RateType> rateTypeList = new ArrayList<>();
            RateType r1 = new RateType();
            r1.setMonths(12);
            r1.setRatio(0.04);
            RateType r2 = new RateType();
            r2.setMonths(18);
            r2.setRatio(0.06);
            RateType r3 = new RateType();
            r3.setMonths(24);
            r3.setRatio(0.08);
            RateType r4 = new RateType();
            r4.setMonths(30);
            r4.setRatio(0.1);
            RateType r5 = new RateType();
            r5.setMonths(36);
            r5.setRatio(0.12);
            RateType r6 = new RateType();
            r6.setMonths(48);
            r6.setRatio(0.16);
            RateType r7 = new RateType();
            r7.setMonths(60);
            r7.setRatio(0.20);
            rateTypeList.add(r1);
            rateTypeList.add(r2);
            rateTypeList.add(r3);
            rateTypeList.add(r4);
            rateTypeList.add(r5);
            rateTypeList.add(r6);
            rateTypeList.add(r7);
            BusinessRate businessRate = new BusinessRate();
            businessRate.setSourceRateId(BusinessRate.DEF_SOURCE_RATE_ID);
            businessRate.setRateTypeList(rateTypeList);
            List<BusinessRate> _rateTypeList = new ArrayList<BusinessRate>();
            _rateTypeList.add(businessRate);
            businessType.setRateTypes(_rateTypeList);
        }
        System.out.println("-----------BusinessType-------------迁移完成-----------------------------");
        return null;
    }
}
