package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.CompensatoryPolicy;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * Created by admin on 2017/6/2.
 */
public interface ICompensatoryPolicyService extends IBaseDataService<CompensatoryPolicy> {

    List<CompensatoryPolicy> findByCarBrandAndDate(String carBrandId, String date);


//    Page<CompensatoryPolicy> findPageByDate(String date,int pageIndex,int pageSize);

    Page<CompensatoryPolicy> findByDate(String date, int currentPage, int currentSize);

    List<CompensatoryPolicy> findAllByCarBrandAndDate(String carBrandId, String createTime);
}
