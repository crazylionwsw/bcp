package com.fuze.bcp.statistics.repository;

import com.fuze.bcp.repository.BaseRepository;
import com.fuze.bcp.statistics.domain.BalanceAccount;

/**
 * Created by GQR on 2017/11/7.
 */
public interface BalanceAccountRepository extends BaseRepository<BalanceAccount,String> {

     BalanceAccount findOneByYearAndMonth(String year,String month);

     BalanceAccount findByCustomerTransactionId(String customerTransactionId);
}
