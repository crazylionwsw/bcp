package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.RepaymentWay;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

/**
 * Created by CJ on 2017/6/12.
 */
public interface IRepaymentWayService extends IBaseDataService<RepaymentWay> {

    Page<RepaymentWay> getAllOrderByAsc(Integer currentPage);
}
