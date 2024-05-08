package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.DealerEmployee;
import com.fuze.bcp.bd.repository.DealerEmployeeRepository;
import com.fuze.bcp.bd.service.IDealerEmployeeService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by CJ on 2017/6/14.
 */
@Service
public class DealerEmployeeServiceImpl extends BaseDataServiceImpl<DealerEmployee, DealerEmployeeRepository> implements IDealerEmployeeService {

    @Autowired
    DealerEmployeeRepository dealerEmployeeRepository;

    @Override
    public Page<DealerEmployee> getAllByDealerEmployees(String carDealerId, Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20);
        return dealerEmployeeRepository.findAllByCarDealerId(carDealerId, pr);

    }

    @Override
    public Integer countDealerEmployees(String carDealerId) {
        return dealerEmployeeRepository.countByDataStatusAndCarDealerId(DataStatus.SAVE,carDealerId);
    }

    @Override
    public List<DealerEmployee> getAllDealerEmployees(String carDealerId) {
        return dealerEmployeeRepository.findByDataStatusAndCarDealerId(DataStatus.SAVE, carDealerId);

    }

}
