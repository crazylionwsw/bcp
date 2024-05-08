package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.CompensatoryPolicy;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by zxp on 2017/3/5.
 */
public interface CompensatoryPolicyRepository extends BaseDataRepository<CompensatoryPolicy, String> {

    List<CompensatoryPolicy> findOneByDataStatusAndCarBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer ds, String carBrandId, String date1, String date2);


    Page<CompensatoryPolicy> findByDataStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer ds, String date1, String date2,Pageable pageable);


    List<CompensatoryPolicy> findByDataStatusAndCarBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer save, String carBrandId, String createTime, String createTime1);
}
