package com.fuze.bcp.statistics.repository;

import com.fuze.bcp.repository.BaseRepository;
import com.fuze.bcp.statistics.domain.ChargeFeePlan;

import java.util.List;

/**
 * Created by GQR on 2017/10/23.
 */

public interface ChargeFeePlanRepository extends BaseRepository<ChargeFeePlan, String> {

    //ChargeFeePlan findOneByCustomerTransactionId(String transactionId);

    List<ChargeFeePlan> findAllByStatusOrderByCarDealerId(Integer status);

    List<ChargeFeePlan> findByStatus(Integer status);
    List<ChargeFeePlan> findAllByStatusAndOrderTimeBefore(Integer status , String beforeDate);

    List<ChargeFeePlan> findAllByStatusAndSwingCardDateBeforeOrderByCarDealerId(Integer status,String beforeDate);

    ChargeFeePlan findOneByIdAndStatus(String id, Integer status);

    ChargeFeePlan findOneByCustomerTransactionId(String transactionId);
}
