package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.PickupCar;
import com.fuze.bcp.creditcar.repository.PickupCarRepository;
import com.fuze.bcp.creditcar.service.IPickupCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by GQR on 2017/8/19.
 */
@Service
public class PickupCarServiceImpl extends BaseBillServiceImpl<PickupCar, PickupCarRepository> implements IPickupCarService {

    @Autowired
    private PickupCarRepository pickupCarRepository;

    @Override
    public Page<PickupCar> findAllByPickupCarByTsDesc(int currentPage) {
        PageRequest pageRequest = new PageRequest(currentPage,20);
        return pickupCarRepository.findAllByOrderByTsDesc(pageRequest);
    }

    @Override
    public List<PickupCar>  getPickupCarsByCustomer(Integer ds, List<String> ids) {
        return pickupCarRepository.findByDataStatusAndCustomerIdIn(ds,ids);
    }

    @Override
    public Page<PickupCar> findAllByApproveStatusAndPickupCarByTsDesc(int currentPage, int approveStatus) {
        PageRequest pageRequest = new PageRequest(currentPage,20);
        return pickupCarRepository.findAllByDataStatusAndApproveStatusOrderByTsDesc(DataStatus.SAVE,approveStatus,pageRequest);
    }

    @Override
    public Page<PickupCar> findAllByApproveStatusAndCustomerIds(int currentPage, int approveStatus, List<String> customerIds) {
        PageRequest pageRequest = new PageRequest(currentPage,20);
        return pickupCarRepository.findAllByDataStatusAndApproveStatusAndCustomerIdInOrderByTsDesc(DataStatus.SAVE,approveStatus,customerIds,pageRequest);
    }

    @Override
    public PickupCar findPickupCarByCustomerTransactionId(String customerTransactionId) {
        return pickupCarRepository.findOneByCustomerTransactionId(customerTransactionId);
    }
}
