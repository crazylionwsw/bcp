package com.fuze.bcp.statistics.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.statistics.domain.BalanceAccount;
import com.fuze.bcp.statistics.repository.BalanceAccountRepository;
import com.fuze.bcp.statistics.service.IBalanceAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Created by GQR on 2017/11/7.
 */
@Service
public class BalanceAccountServiceImpl extends BaseServiceImpl<BalanceAccount, BalanceAccountRepository> implements IBalanceAccountService {

    @Autowired
    BalanceAccountRepository balanceAccountRepository;

    @Override
    public BalanceAccount findOneByYearAndMonth(String year, String month) {
        return balanceAccountRepository.findOneByYearAndMonth(year,month);
    }

    @Override
    public BalanceAccount saveOneBalanceAccount(BalanceAccount balanceAccount) {
        return balanceAccountRepository.save(balanceAccount);
    }

    @Override
    public void deleteByBalanceAccountId(String id) {
            balanceAccountRepository.delete(id);
    }


    @Override
    public Page<BalanceAccount> findAll(Integer currentPage) {
        Pageable pageable = new PageRequest(currentPage,20, BalanceAccount.getTsSortASC());
        return balanceAccountRepository.findAllByOrderByTsDesc(pageable);
    }

    @Override
    public Page<BalanceAccount> findAllByCheckStatus(Integer status, Integer currentPage) {
        Pageable pageable = new PageRequest(currentPage,20, BalanceAccount.getTsSortASC());
        return balanceAccountRepository.findByDataStatus(status,pageable);
    }

    @Override
    public BalanceAccount findByCustomerTransactionId(String customerTransactionId) {
        return balanceAccountRepository.findByCustomerTransactionId(customerTransactionId);
    }


}
