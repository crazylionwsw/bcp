package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.OverdueRecord;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/10.
 */
public interface OverdueRecordRepository extends BaseBillRepository<OverdueRecord,String>{
    List<OverdueRecord> findByCustomerTransactionIdAndDataStatus(String transactionId, Integer save);
}
