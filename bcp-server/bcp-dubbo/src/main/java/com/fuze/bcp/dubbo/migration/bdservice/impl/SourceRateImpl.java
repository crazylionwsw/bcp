package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.bean.BusinessRate;
import com.fuze.bcp.bean.PayAccount;
import com.fuze.bcp.bean.RateType;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.SourceRate;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Liu} on 2017/10/19.
 */
public class SourceRateImpl implements SourceRate {

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

        com.fuze.bcp.bd.domain.SourceRate sr = (com.fuze.bcp.bd.domain.SourceRate) entity;
        //存值
        if (sourceMap.getString("_id").equals(BusinessRate.DEF_SOURCE_RATE_ID)) {
            sr.setId(sourceMap.getString("_id"));
            sr.setCode("001");
            sr.setName("新车费率");
            sr.setStartDate("2017-01-01");
            sr.setEndDate("2017-12-31");
            sr.setAmount(1000000000.0);
            sr.setCashSourceId("589ad090acd23edf388a0cf8");
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
            sr.setRateTypes(rateTypeList);
        } else if (sourceMap.getString("_id").equals(BusinessRate.DEF_SOURCE_RATE_ID_2)) {
            sr.setId(sourceMap.getString("_id"));
            sr.setCode("002");
            sr.setName("二手车费率");
            sr.setStartDate("2017-01-01");
            sr.setEndDate("2017-12-31");
            sr.setAmount(1000000000.0);
            sr.setCashSourceId("589ad090acd23edf388a0cf8");
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
            sr.setRateTypes(rateTypeList1);
        }
        System.out.println("-----------SourceRate-------------迁移完成-----------------------------");

        return null;
    }
}
