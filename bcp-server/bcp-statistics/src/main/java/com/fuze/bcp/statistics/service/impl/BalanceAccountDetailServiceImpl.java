package com.fuze.bcp.statistics.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.statistics.domain.BalanceAccountDetail;
import com.fuze.bcp.statistics.repository.BalanceAccountDetailRepository;
import com.fuze.bcp.statistics.service.IBalanceAccountDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by ${Liu} on 2018/1/2.
 */
@Service
public class BalanceAccountDetailServiceImpl extends BaseServiceImpl<BalanceAccountDetail,BalanceAccountDetailRepository> implements IBalanceAccountDetailService {

    @Autowired
    BalanceAccountDetailRepository balanceAccountDetailRepository;


    @Override
    public BalanceAccountDetail findOneByCustomerTransactionIdAndBalanceAccountId(String transactionId, String balanceAccountId) {
        return balanceAccountDetailRepository.findOneByCustomerTransactionIdAndBalanceAccountId(transactionId,balanceAccountId);
    }

    @Override
    public BalanceAccountDetail saveOneBalanceAccountDetail(BalanceAccountDetail balanceAccountDetail) {
        return balanceAccountDetailRepository.save(balanceAccountDetail);
    }

    @Override
    public List<BalanceAccountDetail> getOneDetail(String balanceId) {
        return balanceAccountDetailRepository.findOneByBalanceAccountId(balanceId);
    }

    @Override
    public List<BalanceAccountDetail> findOneByBalanceAccountId(String id, Sort sort) {
        return balanceAccountDetailRepository.findOneByBalanceAccountId(id,sort);
    }

    @Override
    public List<BalanceAccountDetail> findAllBalanceAccountDetail(String transactionId) {
        return balanceAccountDetailRepository.findAllBycustomerTransactionId(transactionId);
    }

    @Override
    public BalanceAccountDetail getBalanceAccountDetail(String transactionId) {
        return balanceAccountDetailRepository.findByDataStatusAndCustomerTransactionId(DataStatus.SAVE,transactionId);
    }

}
