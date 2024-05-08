package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.CashSourceEmployee;
import com.fuze.bcp.service.IBaseDataService;

import java.util.List;

/**
 * Created by admin on 2017/6/2.
 */
public interface ICashSourceEmployeeService extends IBaseDataService<CashSourceEmployee> {

    List<CashSourceEmployee> getByCashSourceId(String cashSourceId);
}
