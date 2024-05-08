package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.BillType;
import com.fuze.bcp.bd.repository.BillTypeRepository;
import com.fuze.bcp.bd.service.IBillTypeService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2017/5/26.
 */
@Service
public class BillTypeServiceImpl extends BaseDataServiceImpl<BillType, BillTypeRepository> implements IBillTypeService {
    @Autowired
    BillTypeRepository billTypeRepository;

    @Override
    public Page<BillType> getAllOrderBy(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage,20);
        return billTypeRepository.findAllByOrderByCodeAsc(pr);
    }
}
