package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.CustomerPackage;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

/**
 * Created by hecaifeng on 2017/2/18.
 */
public interface CustomerPackageRepository extends BaseRepository<CustomerPackage,String> {

    List<CustomerPackage> findAllByCustomerId(String customerId);

}
