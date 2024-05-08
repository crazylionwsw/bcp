package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.creditcar.repository.CustomerDemandRepository;
import com.fuze.bcp.creditcar.service.ICustomerDemandService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lily on 2017/7/19.
 */
@Service
public class CustomerDemandServiceImpl extends BaseBillServiceImpl<CustomerDemand, CustomerDemandRepository> implements ICustomerDemandService {

    @Override
    public CustomerDemand findCarDemandByMateCustomerId(String customerId) {
        return baseRepository.findByMateCustomerId(customerId);
    }

    @Override
    public List<CustomerDemand> findAllByMateCustomerId(String customerId) {
        return baseRepository.findAllByMateCustomerId(customerId);
    }

    @Override
    public CustomerDemand findByVehicleEvaluateInfoId(String id) {
        return baseRepository.findByVehicleEvaluateInfoIdAndDataStatus(id, DataStatus.SAVE);
    }
//要写
    @Override
    public List<CustomerDemand> findByDataStatusAndApproveStatusAndIdNotInAndTsLessThan(Integer ds, Integer as, List<String> ids, String date) {
        return null;
    }

    @Override
    public List<CustomerDemand> findAllByDataStatusAndApproveStatus(Integer ds, Integer as, Sort sort) {
        return baseRepository.findAllByDataStatusAndApproveStatus(ds,as,CustomerDemand.getTsSort());
    }

    @Override
    public List<CustomerDemand> findAllByTsDesc(Sort sort) {
        return baseRepository.findAll(CustomerDemand.getTsSort());
    }

    @Override
    public List<CustomerDemand> findAllBypledgeCustomerId(String customerId) {
        return baseRepository.findAllBypledgeCustomerId(customerId);
    }

}
