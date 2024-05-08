package com.fuze.bcp.statistics.repository;

import com.fuze.bcp.repository.BaseRepository;
import com.fuze.bcp.statistics.domain.ChargeFeePlan;
import com.fuze.bcp.statistics.domain.ChargeFeePlanDetail;

import java.util.List;

/**
 * Created by GQR on 2017/10/31.
 */
public interface ChargeFeePlanDetailRepository extends BaseRepository<ChargeFeePlanDetail,String> {

    void deleteByChargeFeePlanId(String chargefeeplanId);

    List<ChargeFeePlanDetail> findOneByChargeFeePlanId(String chargeFeePlanId);

    List<ChargeFeePlanDetail> findAllByYearAndMonthAndDataStatus(String month,String year,Integer ds);

    ChargeFeePlan findByCustomerTransactionId(String customerTransactionId);
}
