package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.PoundageSettlement;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by zqw on 2017/8/29.
 */
public interface PoundageSettlementRepository extends BaseDataRepository<PoundageSettlement,String>{

    Page<PoundageSettlement> findAllByOrderByOrderTimeDesc(Pageable pageable);

    List<PoundageSettlement> getAllBySettlementCashSourceIdAndChargePaymentWayCode(String settlementCashSourceId, String chargePaymentWayCode);

    List<PoundageSettlement> findAllByDataStatusAndTsStartingWith(Integer ds, String today);

    Page<PoundageSettlement> findByDataStatusAndCustomerIdInOrderByOrderTimeDesc(Integer save, List<String> customerIds, Pageable pageable);

    PoundageSettlement findOneByCustomerTransactionId(String customerTransactionId);

    PoundageSettlement findOneByCustomerTransactionIdAndDataStatus(String customerTransactionId, int save);

    Page<PoundageSettlement> getAllByOrderByOrderTimeDesc(Pageable page);
}
