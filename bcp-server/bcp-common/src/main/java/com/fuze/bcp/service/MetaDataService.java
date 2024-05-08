package com.fuze.bcp.service;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/8/10.
 */
@Service
public class MetaDataService {

    public static final String ID = "_id";

    /**
     * 定义在 系统参配项中  METADATAS
     * 如果新增，需要在其中配置
     * TODO:修改名称
     */
    public static final String CUSTOMERTRANSACTION = "transaction";
    public static final String EMPLOYEE = "employee";
    public static final String DEALEREMPLOYEE = "dealeremployee";
    public static final String ORGINFO = "orginfo";
    public static final String SUPERORGINFO = "superorginfo";
    public static final String EMPLOYEELEADER = "employeeleader";
    public static final String CUSTOMER = "customer";
    public static final String MATECUSTOMER = "matecustomer";
    public static final String PLEDGECUSTOMER = "pledgecustomer";
    public static final String PLEDGE = "pledge";//WORD专用
    public static final String GUARANTEECUSTOMER = "guaranteecustomer";
    public static final String CARDEALER = "cardealer";
    public static final String CASHSOURCE = "cashsource";
    public static final String CUSTOMERCARD = "customercard";
    public static final String CUSTOMERLOAN = "customerloan";
    public static final String CUSTOMERCAR = "customercar";
    public static final String CARBRAND = "carbrand";
    public static final String CARMODEL = "carmodel";
    public static final String CARTYPE = "cartype";
    public static final String CARVALUATION = "carvaluation";
    public static final String CUSTOMERDEMAND = "customerdemand";
    public static final String PURCHASECARORDER = "purchasecarorder";
    public static final String BANKCARDAPPLY = "bankcardapply";
    public static final String DECLARATION = "declaration";
    public static final String APPOINTPAYMENT = "appointpayment";
    public static final String IMAGEPATH = "imagepath";
    public static final String CUSTOMERIMAGES = "customerimages";
    public static final String RECEPTFILE = "receptfile";
    public static final String SURVEYRESULT = "surveyresult";
    public static final String TEMPLATEIMAGES = "templateimages";
    public static final String DECLARATIONHISTORYS = "declaration_historys";
    public static final String CANCALORDER = "cancelorder";
    public static final String APPROVEUSER = "approveuser";
    public static final String DMVPLEDGE = "dmvpledge";
    public static final String CREDITPHOTOGRAPH = "creditphotograph";


    @Autowired
    MongoTemplate mongoTemplate;
//    批处理代码
//    public void bathUpdate() {
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<BathUpdateOptions> list = new ArrayList<BathUpdateOptions>();
//                List<BasicDBObject> cartypes = mongoTemplate.find(Query.query(new Criteria()), BasicDBObject.class, "bd_cartype");
//                int i = 0;
//                for (BasicDBObject cartype : cartypes) {
//                    BasicDBObject carModel = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(new ObjectId(cartype.get("carModelId").toString()))),BasicDBObject.class, "bd_carmodel");
//                    if(carModel != null){
//                        System.out.println(i++ + ":" + cartype.get("_id")+":"+carModel.get("fullName")+" "+cartype.get("name"));
//                        list.add(new BathUpdateOptions(Query.query(Criteria.where("_id").is(new ObjectId(cartype.get("_id").toString()))),
//                                Update.update("fullName", carModel.get("fullName")+" "+cartype.get("name")), true, true));
//                    }else{
//                        System.out.println(i++ + ":" + cartype.get("_id")+":"+cartype.get("name"));
//                        list.add(new BathUpdateOptions(Query.query(Criteria.where("_id").is(new ObjectId(cartype.get("_id").toString()))),
//                                Update.update("fullName", cartype.get("name")), true, true));
//                    }
//                    if(i%800 == 0){
//                        int n = BathUpdateOptions.bathUpdate(mongoTemplate, "bd_cartype", list);
//                        list.clear();
//                    }
//                }
//                int n = BathUpdateOptions.bathUpdate(mongoTemplate, "bd_cartype", list);
//                list.clear();
//                System.out.println("受影响的行数：" + n);
//            }
//        });
//
//        thread.start();
//    }

    public BasicDBObject getObjMap(String field, String value, String collectionName) {
        Query query = new Query();
        if (ID.equals(field) && value != null) {
            query.addCriteria(Criteria.where(field).is(new ObjectId(value)));
        } else {
            query.addCriteria(Criteria.where(field).is(value));
        }
        query.addCriteria(Criteria.where("dataStatus").in(0,1));
        List<BasicDBObject> objs = mongoTemplate.find(query, BasicDBObject.class, collectionName);
        if (objs.size() == 0) {
            return null;
        }
        if (objs.size() == 1 ) {
            return objs.get(0);
        }
        throw new RuntimeException("getObjMap get more then one obj,field:"+field+",value:"+ value);
    }

    public List<BasicDBObject> getObjMapMore(String field, String value, String collectionName) {
        Query query = new Query();
        if (ID.equals(field) && value != null) {
            query.addCriteria(Criteria.where(field).is(new ObjectId(value)));
        } else {
            query.addCriteria(Criteria.where(field).is(value));
        }
        query.addCriteria(Criteria.where("dataStatus").in(0,1));
        List<BasicDBObject> objs = mongoTemplate.find(query, BasicDBObject.class, collectionName);
        if (objs.size() == 0) {
            return null;
        }
        if (objs.size() > 0 ) {
            return objs;
        }
        throw new RuntimeException("getObjMap get more then one obj,field:"+field+",value:"+ value);
    }

}
