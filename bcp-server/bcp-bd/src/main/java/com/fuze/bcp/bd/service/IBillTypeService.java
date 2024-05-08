package com.fuze.bcp.bd.service;


import com.fuze.bcp.bd.domain.BillType;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

/**
 * Created by admin on 2017/5/24.
 */
public interface IBillTypeService extends IBaseDataService<BillType> {

    Page<BillType> getAllOrderBy(Integer currentPage);


}
