package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 购车签约单
 * Created by sean on 2016/11/29.
 */
public interface PurchaseCarOrderRepository extends BaseBillRepository<PurchaseCarOrder,String> {

    Page<PurchaseCarOrder> findByDataStatusOrderByTsDesc(Integer save, Pageable pageable);

    Page<PurchaseCarOrder> findByDataStatusAndApproveStatusAndCustomerIdIn(Integer save, int approveStatus, List<String> customerIds, Pageable pageable);

    PurchaseCarOrder findOneByCustomerIdAndCustomerTransactionIdAndDataStatus(String customerId, String customerTransactionId, Integer save);

    Page<PurchaseCarOrder> findByDataStatusAndLoginUserId(Integer save, Pageable pageable);

    PurchaseCarOrder findByVehicleEvaluateInfoIdAndDataStatus(String id, Integer ds);

    PurchaseCarOrder findByVehicleEvaluateInfoId(String id);

    List<PurchaseCarOrder> findAllByVehicleEvaluateInfoId(String id);

    List<PurchaseCarOrder> findAllByVehicleEvaluateInfoIdAndDataStatus(String id, Integer ds);

}
