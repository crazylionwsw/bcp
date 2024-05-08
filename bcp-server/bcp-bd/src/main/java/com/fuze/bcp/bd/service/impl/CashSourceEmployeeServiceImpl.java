package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.CashSourceEmployee;
import com.fuze.bcp.bd.repository.CashSourceEmployeeRepository;
import com.fuze.bcp.bd.service.ICashSourceEmployeeService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/6/14.
 */
@Service
public class CashSourceEmployeeServiceImpl extends BaseDataServiceImpl<CashSourceEmployee, CashSourceEmployeeRepository> implements ICashSourceEmployeeService {

    @Autowired
    CashSourceEmployeeRepository cashSourceEmployeeRepository;

    @Override
    public List<CashSourceEmployee> getByCashSourceId(String cashSourceId) {
        return cashSourceEmployeeRepository.findAllByCashSourceId(cashSourceId);
    }
}
