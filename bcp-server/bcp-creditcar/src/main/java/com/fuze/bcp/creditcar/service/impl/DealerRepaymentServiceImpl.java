package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.DealerRepayment;
import com.fuze.bcp.creditcar.repository.DealerRepaymentRepository;
import com.fuze.bcp.creditcar.service.IDealerRepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lily on 2017/9/23.
 */
@Service
public class DealerRepaymentServiceImpl extends BaseBillServiceImpl<DealerRepayment,DealerRepaymentRepository> implements IDealerRepaymentService {

    @Autowired
    DealerRepaymentRepository dealerRepaymentRepository;

    @Override
    public Page<DealerRepayment> findAllByStatusOrderByTsDesc(int status, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20,DealerRepayment.getTsSort());
        return dealerRepaymentRepository.findAllByStatusAndDataStatusOrderByTsDesc(status, DataStatus.SAVE, page);
    }

    @Override
    public Page<DealerRepayment> findAllByStatusAndCustomerIds(List<String> customerIds, int status, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20,DealerRepayment.getTsSort());
        return dealerRepaymentRepository.findAllByDataStatusAndStatusAndCustomerIdInOrderByTsDesc(DataStatus.SAVE, status, customerIds, page);
    }

    @Override
    public Page<DealerRepayment> findByLoginUserIdAndApproveStatusIn(String loginUserId, List<Integer> as, Integer currentPage, Integer currentSize) {
        PageRequest page = new PageRequest(currentPage, 20,DealerRepayment.getTsSort());
        return dealerRepaymentRepository.findByLoginUserIdAndApproveStatusIn(loginUserId,as,page);
    }
}
