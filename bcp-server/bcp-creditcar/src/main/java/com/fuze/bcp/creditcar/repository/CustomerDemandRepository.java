package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.CustomerDemand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 资质审核业务单
 * Created by sean on 2016/11/29.
 */
public interface CustomerDemandRepository extends BaseBillRepository<CustomerDemand,String> {

    CustomerDemand findByMateCustomerId(String customerId);

    List<CustomerDemand> findAllByMateCustomerId(String customerId);

    CustomerDemand findCustomerDemandByCustomerId(String customerId);

    CustomerDemand findByVehicleEvaluateInfoIdAndDataStatus(String id, Integer ds);

    Page<CustomerDemand> findByDataStatusAndCustomerIdIn(Integer ds,List<String> customerIds, Pageable page);

    Page<CustomerDemand> findByDataStatusAndApproveStatusAndCustomerIdIn(Integer ds,Integer approveStatus,List<String> customerIds,  Pageable page);

    List<CustomerDemand> findAllBypledgeCustomerId(String customerId);
}
