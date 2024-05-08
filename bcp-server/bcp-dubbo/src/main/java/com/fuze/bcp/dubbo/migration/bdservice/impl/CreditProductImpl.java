package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.CreditProduct;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Liu} on 2017/10/20.
 */
public class CreditProductImpl implements CreditProduct{

    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {

        com.fuze.bcp.bd.domain.CreditProduct creditProduct = (com.fuze.bcp.bd.domain.CreditProduct) entity;

        //取值
        String cashSourceId = sourceMap.get("cashSourceId") != null ? (String) sourceMap.get("cashSourceId") : null;
        String code = sourceMap.getString("code");
        Integer dataStatus = sourceMap.getInt("dataStatus");
        String name = sourceMap.getString("name");
        String startDate = sourceMap.getString("startDate");
        String ts = sourceMap.getString("ts");
        String endDate = sourceMap.getString("endDate");

        List guaranteeWays = sourceMap.get("guaranteeWays") != null ? (List) sourceMap.get("guaranteeWays") : null;
        List guaranteeWayIdes = new ArrayList();
        for (Object gw:guaranteeWays) {
            com.mongodb.DBRef gwDBRef = (com.mongodb.DBRef) gw;
            BasicDBObject billObj = source.findOne(new Query(Criteria.where("_id").is(new ObjectId(gwDBRef.getId().toString()))), BasicDBObject.class, gwDBRef.getCollectionName());
            guaranteeWayIdes.add(billObj.get("_id").toString());
        }

        List repaymentWays = sourceMap.get("repaymentWays") != null ? (List) sourceMap.get("repaymentWays") : null;
        List repaymentWayIds = new ArrayList();
        for (Object rw:repaymentWays) {
            com.mongodb.DBRef rwDBRef = (com.mongodb.DBRef) rw;
            BasicDBObject rwObj = source.findOne(new Query(Criteria.where("_id").is(new ObjectId(rwDBRef.getId().toString()))), BasicDBObject.class, rwDBRef.getCollectionName());
            repaymentWayIds.add(rwObj.get("_id").toString());
        }

        List<String> provinceIds = sourceMap.get("provinceIds") != null ? (List) sourceMap.get("provinceIds") : null;
        List pList = new ArrayList<>();
        if(provinceIds != null){
            for (String pId:provinceIds) {
                pList.add(pId);
            }
        }

        //存值
        creditProduct.setId(sourceMap.getString("_id"));
        creditProduct.setCashSourceId(cashSourceId);
        creditProduct.setCode(code);
        creditProduct.setDataStatus(dataStatus);
        creditProduct.setName(name);
        creditProduct.setStartDate(startDate);
        creditProduct.setTs(ts);
        creditProduct.setEndDate(endDate);
        creditProduct.setGuaranteeWayIds(guaranteeWayIdes);
        creditProduct.setRepaymentWayIds(repaymentWayIds);
        creditProduct.setProvinceIds(pList);

        System.out.println("----------CreditProduct--------------迁移完成-----------------------------");

        return null;
    }
}
