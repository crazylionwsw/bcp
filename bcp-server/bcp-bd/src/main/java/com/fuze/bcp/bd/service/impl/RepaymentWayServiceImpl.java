package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.RepaymentWay;
import com.fuze.bcp.bd.repository.RepaymentWayRepository;
import com.fuze.bcp.bd.service.IRepaymentWayService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/6/12.
 */
@Service
public class RepaymentWayServiceImpl extends BaseDataServiceImpl<RepaymentWay, RepaymentWayRepository> implements IRepaymentWayService {

    @Autowired
    RepaymentWayRepository repaymentWayRepository;

    @Override
    public Page<RepaymentWay> getAllOrderByAsc(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage,20);
        return repaymentWayRepository.findAllByOrderByCodeAsc(pr);
    }
}
