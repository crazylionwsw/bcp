package com.fuze.bcp.cardealer.service;

import com.fuze.bcp.cardealer.domain.DealerGroup;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

/**
 * Created by admin on 2017/5/24.
 */
public interface IDealerGroupService extends IBaseDataService<DealerGroup> {

    Page<DealerGroup> getAllOrderByTs(Integer currentPage);

}
