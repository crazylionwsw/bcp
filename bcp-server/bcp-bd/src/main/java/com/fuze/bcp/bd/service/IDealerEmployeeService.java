package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.CarModel;
import com.fuze.bcp.bd.domain.DealerEmployee;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by CJ on 2017/6/14.
 */
public interface IDealerEmployeeService extends IBaseDataService<DealerEmployee> {

    Page<DealerEmployee> getAllByDealerEmployees(String carDealerId, Integer currentPage);

    Integer countDealerEmployees(String carDealerId);

    List<DealerEmployee> getAllDealerEmployees(String carDealerId);

}
