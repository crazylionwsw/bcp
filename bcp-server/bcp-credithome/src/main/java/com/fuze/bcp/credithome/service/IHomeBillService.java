package com.fuze.bcp.credithome.service;

import com.fuze.bcp.credithome.domain.HomeBillEntity;
import com.fuze.bcp.service.IBaseService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by admin on 2017/9/11.
 */
public interface IHomeBillService<T extends HomeBillEntity> extends IBaseService<T> {

    T findByCustomerTransactionId(String transactionId);

    T findAvailableOneByCustomerTransactionId(String transactionId);

    List<String> getAllIdListByDataStatus(int dataStatus, String collectionName, String propertyName);

    List<ObjectId> getIdsList(Query query, String collectionName, String propertyName);

    List<?> getAllFieldList(Query query, String collectionName, String propertyName);

}
