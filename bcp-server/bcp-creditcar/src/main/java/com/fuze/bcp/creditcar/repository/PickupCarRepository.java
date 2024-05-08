package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.PickupCar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by sean on 2016/11/29.
 */
public interface PickupCarRepository extends BaseBillRepository<PickupCar,String> {

    Page<PickupCar> findAllByDataStatusAndApproveStatusOrderByTsDesc(Integer save, int approveStatus, Pageable pageable);

    Page<PickupCar> findAllByDataStatusAndApproveStatusAndCustomerIdInOrderByTsDesc(Integer save, int approveStatus, List<String> customerIds, Pageable pageable);
}

