package com.fuze.bcp.statistics.service;

import com.fuze.bcp.service.IBaseService;
import com.fuze.bcp.statistics.domain.BalanceAccount;
import org.springframework.data.domain.Page;

/**
 * Created by GQR on 2017/11/7.
 */
public interface IBalanceAccountService  extends IBaseService<BalanceAccount> {

    BalanceAccount findOneByYearAndMonth(String year,String month);

    BalanceAccount saveOneBalanceAccount(BalanceAccount balanceAccount);

    void deleteByBalanceAccountId(String id);

    Page<BalanceAccount> findAll(Integer currentPage);

    Page<BalanceAccount> findAllByCheckStatus(Integer status, Integer currentPage);


    BalanceAccount findByCustomerTransactionId(String id);
}
