package com.fuze.bcp.cardealer.service.impl;

import com.fuze.bcp.cardealer.domain.DealerGroup;
import com.fuze.bcp.cardealer.repository.DealerGroupRepository;
import com.fuze.bcp.cardealer.service.IDealerGroupService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2017/5/26.
 */
@Service
public class DealerGroupServiceImpl extends BaseDataServiceImpl<DealerGroup, DealerGroupRepository> implements IDealerGroupService {

    @Autowired
    DealerGroupRepository dealerSharingRepository;

    @Override
    public Page<DealerGroup> getAllOrderByTs(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20);
        return dealerSharingRepository.findAllByOrderByTsDesc(pr);
    }
}
