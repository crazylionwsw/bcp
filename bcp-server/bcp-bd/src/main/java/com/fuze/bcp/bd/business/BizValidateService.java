package com.fuze.bcp.bd.business;

import com.alibaba.dubbo.config.annotation.Service;
import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.api.bd.service.IValidateBizService;
import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.ResultBean;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by ${Liu} on 2018/3/23.
 */
@Service
public class BizValidateService implements IValidateBizService{
    private static Logger logger = LoggerFactory.getLogger(BizValidateService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResultBean<String> actCheckExist(APIBaseBean baseBean) {
        MongoEntity mongoEntity = baseBean.getClass().getAnnotation(MongoEntity.class);
        String entityName = mongoEntity.entityName();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(baseBean.getId())));
        BasicDBObject obj = mongoTemplate.findOne(query, BasicDBObject.class, entityName);
        if (obj == null) {
            return ResultBean.getSucceed().setD(Boolean.FALSE.toString());
        } else {
            return ResultBean.getSucceed().setD(Boolean.TRUE.toString());
        }
    }

    @Override
    public ResultBean<String> actCheckUnique(APIBaseBean baseBean, String propname, Object val) {
        MongoEntity mongoEntity = baseBean.getClass().getAnnotation(MongoEntity.class);
        String entityName = mongoEntity.entityName();
        Criteria criteria = Criteria.where(propname).is(val);
        if (baseBean.getId() != null) {
            criteria.andOperator(Criteria.where("_id").ne(new ObjectId(baseBean.getId())));
        }
        Query query = new Query();
        query.addCriteria(criteria);
        BasicDBObject obj = mongoTemplate.findOne(query, BasicDBObject.class, entityName);
        if (obj != null) {
            return ResultBean.getSucceed().setD(Boolean.FALSE.toString());
        } else {
            return ResultBean.getSucceed().setD(Boolean.TRUE.toString());
        }
    }
}
