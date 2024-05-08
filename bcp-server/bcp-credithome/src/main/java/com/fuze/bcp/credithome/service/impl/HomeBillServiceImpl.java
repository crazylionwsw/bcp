package com.fuze.bcp.credithome.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.credithome.domain.HomeBillEntity;
import com.fuze.bcp.credithome.repository.HomeBillRepository;
import com.fuze.bcp.credithome.service.IHomeBillService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 2017/7/19.
 */
public class HomeBillServiceImpl<T extends HomeBillEntity, R extends HomeBillRepository<T, String>> extends BaseServiceImpl<T, R> implements IHomeBillService<T> {

    private static final Logger logger = LoggerFactory.getLogger(HomeBillServiceImpl.class);

    @Override
    public T findByCustomerTransactionId(String transactionId) {
        return baseRepository.findOneByCustomerTransactionId(transactionId);
    }

    @Override
    public T findAvailableOneByCustomerTransactionId(String transactionId) {
        return baseRepository.findAvailableOneByCustomerTransactionIdAndDataStatus(transactionId,DataStatus.SAVE);
    }

    /**
     * 根据dataStatus获取全部的数据，然后获取某个属性的值数组
     *
     * @param dataStatus
     * @param propertyName
     * @return
     */
    public List<String> getAllIdListByDataStatus(int dataStatus, String collectionName, final String propertyName) {
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        final List<String>    ids = new ArrayList<String>();
        mongoTemplate.executeQuery(Query.query(criteria), collectionName, new DocumentCallbackHandler() {
            public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {
                String pvalue = null;
                if ("_id".equals(propertyName)){
                    pvalue = ((ObjectId)dbObject.get(propertyName)).toString();
                } else {
                    pvalue = (String)dbObject.get(propertyName);
                }
                if(pvalue != null){
                    ids.add(pvalue);
                }
                return ;
            }
        });
        return ids;
    }

}
