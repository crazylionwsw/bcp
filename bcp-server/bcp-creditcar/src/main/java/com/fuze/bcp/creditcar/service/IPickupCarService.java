package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.PickupCar;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by GQR on 2017/8/19.
 */
public interface IPickupCarService extends IBaseBillService<PickupCar> {

    Page<PickupCar> findAllByPickupCarByTsDesc(int currentPage);

    List<PickupCar> getPickupCarsByCustomer(Integer ds, List<String> ids);

    PickupCar findPickupCarByCustomerTransactionId(String customerTransactionId);

    Page<PickupCar> findAllByApproveStatusAndPickupCarByTsDesc(int currentPage, int approveStatus);

    Page<PickupCar> findAllByApproveStatusAndCustomerIds(int currentPage, int approveStatus, List<String> customerIds);

    List<String> getAllIdListByDataStatus(int dataStatus,String collectionName,String propertyName);

}
