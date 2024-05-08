package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.CustomerDemand;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by Lily on 2017/7/19.
 */
public interface ICustomerDemandService extends IBaseBillService<CustomerDemand> {

    CustomerDemand findCarDemandByMateCustomerId(String customerId);

    List<CustomerDemand> findAllByMateCustomerId(String customerId);

    CustomerDemand findByVehicleEvaluateInfoId(String id);

    List<CustomerDemand>  findByDataStatusAndApproveStatusAndIdNotInAndTsLessThan(Integer ds,Integer as,List<String> ids ,String date);

    List<CustomerDemand> findAllByDataStatusAndApproveStatus(Integer ds, Integer as, Sort sort);

    List<CustomerDemand> findAllByTsDesc(Sort sort);


    List<CustomerDemand> findAllBypledgeCustomerId(String customerId);
}
