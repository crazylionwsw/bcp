package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.Enhancement;
import com.fuze.bcp.creditcar.repository.EnhancementRepository;
import com.fuze.bcp.creditcar.service.IEnhancementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gqr on 2017/8/18.
 */

@Service
public class EnhancementServiceImpl extends BaseBillServiceImpl<Enhancement, EnhancementRepository> implements IEnhancementService {

    @Autowired
    EnhancementRepository enhancementRepository;

    @Override
    public Page<Enhancement> findByLoginUserIdAndApproveStatusIn(String loginUserId, List<Integer> as, Integer currentPage, Integer currentSize, String customerTransactionId) {
        PageRequest page = new PageRequest(currentPage, 20, Enhancement.getTsSort());
        if ("".equals(customerTransactionId)){
            return enhancementRepository.findByLoginUserIdAndApproveStatusIn(loginUserId,as,page);
        } else {
            return enhancementRepository.findAllByLoginUserIdAndApproveStatusInAndCustomerTransactionId(loginUserId,as,customerTransactionId,page);
        }
    }
}
