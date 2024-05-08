package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.OverdueRecord;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/10.
 */
public interface IOverdueRecordService extends IBaseBillService<OverdueRecord>{

    OverdueRecord getOverdueRecordByMonth(String transactionId,String month);

    List<OverdueRecord> findByCustomerTransactionIdAndDataStatus(String transactionId, Integer save);

   void delOverdueRecordByTransactionId(String transactionId);
}
