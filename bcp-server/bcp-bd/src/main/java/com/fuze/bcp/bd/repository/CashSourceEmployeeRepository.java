package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.CashSourceEmployee;
import com.fuze.bcp.repository.BaseDataRepository;

import java.util.List;

/**
 * Created by CJ on 2017/6/14.
 */
public interface CashSourceEmployeeRepository extends BaseDataRepository<CashSourceEmployee,String> {
    List<CashSourceEmployee> findAllByCashSourceId(String cashSourceId);
}
