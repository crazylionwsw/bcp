package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.CompensatoryPolicy;
import com.fuze.bcp.bd.repository.CompensatoryPolicyRepository;
import com.fuze.bcp.bd.service.ICompensatoryPolicyService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2017/7/10.
 */
@Service
public class CompensatoryServiceImpl extends BaseDataServiceImpl<CompensatoryPolicy, CompensatoryPolicyRepository> implements ICompensatoryPolicyService {
    @Autowired
    CompensatoryPolicyRepository compensatoryPolicyRepository;

    @Override
    public List<CompensatoryPolicy> findByCarBrandAndDate(String carBrandId, String date) {
        return compensatoryPolicyRepository.findOneByDataStatusAndCarBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(DataStatus.SAVE, carBrandId, date, date);
    }

//    @Override
//    public Page<CompensatoryPolicy> findPageByDate(String date, int pageIndex, int pageSize) {
//        Pageable pageable = new PageRequest(pageIndex, pageSize, CompensatoryPolicy.getTsSort());
//        return compensatoryPolicyRepository.findByDataStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(DataStatus.SAVE, date, date, pageable);
//	}
    public Page<CompensatoryPolicy> findByDate(String date, int currentPage, int currentSize) {
        PageRequest page = new PageRequest(currentPage, currentSize, CompensatoryPolicy.getTsSort());

        return compensatoryPolicyRepository.findByDataStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(DataStatus.SAVE, date, date, page);

    }

    @Override
    public List<CompensatoryPolicy> findAllByCarBrandAndDate(String carBrandId, String createTime) {
        return compensatoryPolicyRepository.findByDataStatusAndCarBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(DataStatus.SAVE, carBrandId, createTime, createTime);
    }
}
