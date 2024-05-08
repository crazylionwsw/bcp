package com.fuze.bcp.credithome.repository;

import com.fuze.bcp.credithome.domain.HomeBillEntity;
import com.fuze.bcp.repository.BaseRepository;

import java.io.Serializable;

/**
 * Created by sean on 2016/11/29.
 */
public interface HomeBillRepository<T extends HomeBillEntity,ID extends Serializable> extends BaseRepository<T,ID> {

    /**
     * 通过客户交易获取指定单据
     * @param customerTransactionId
     * @return
     */
    T findOneByCustomerTransactionId(String id);

    /**
     * 通过客户交易获取指定的可用单据
     * @param customerTransactionId
     * @param status        数据状态
     * @return
     */
    T findAvailableOneByCustomerTransactionIdAndDataStatus(String customerTransactionId,Integer status);

    T findOneByDataStatusAndId(Integer ds, String id);
}
