package com.fuze.bcp.statistics.repository;

import com.fuze.bcp.repository.BaseRepository;
import com.fuze.bcp.statistics.domain.BalanceAccountDetail;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by GQR on 2017/11/7.
 */
public interface BalanceAccountDetailRepository extends BaseRepository<BalanceAccountDetail,String> {

    BalanceAccountDetail findOneByCustomerTransactionIdAndBalanceAccountId(String transactionId,String balanceAccountId);

    List<BalanceAccountDetail> findOneByBalanceAccountId(String balanceAccountId);

    List<BalanceAccountDetail> findOneByBalanceAccountId(String id, Sort sort);

    BalanceAccountDetail findByDataStatusAndCustomerTransactionId(int dt,String transactionId);

    List<BalanceAccountDetail> findAllBycustomerTransactionId(String transactionId);
}
