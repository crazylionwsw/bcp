package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.GuaranteeWay;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

/**
 * Created by CJ on 2017/6/12.
 */
public interface IGuaranteeWayService extends IBaseDataService<GuaranteeWay> {

    Page<GuaranteeWay> getAllOrderByAsc(Integer currentPage);
}
