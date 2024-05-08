package com.fuze.bcp.statistics.service;

import com.fuze.bcp.service.IBaseService;
import com.fuze.bcp.statistics.domain.BalanceAccountDetail;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by ${Liu} on 2018/1/2.
 */
public interface IBalanceAccountDetailService extends IBaseService<BalanceAccountDetail> {

    List<BalanceAccountDetail> getOneDetail(String balanceId);


    List<BalanceAccountDetail> findOneByBalanceAccountId(String id, Sort sort);

    List<BalanceAccountDetail> findAllBalanceAccountDetail(String transactionId);

    BalanceAccountDetail getBalanceAccountDetail(String transactionId);

    BalanceAccountDetail findOneByCustomerTransactionIdAndBalanceAccountId(String transactionId,String balanceAccountId);

    BalanceAccountDetail saveOneBalanceAccountDetail(BalanceAccountDetail balanceAccountDetail);

}
