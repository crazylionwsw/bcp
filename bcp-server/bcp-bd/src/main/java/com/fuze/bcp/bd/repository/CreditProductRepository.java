package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.CreditProduct;
import com.fuze.bcp.repository.BaseDataRepository;

import java.util.List;

/**
 * Created by sean on 2016/11/29.
 */
public interface CreditProductRepository extends BaseDataRepository<CreditProduct,String> {

    /**
     * 根据 合作支行和业务类型 查询贷款产品
     * @param save
     * @param cashSourceIds
     * @return
     */
    List<CreditProduct> findByDataStatusAndCashSourceIdIn(Integer save, List<String> cashSourceIds);
}
