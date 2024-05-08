package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.SharingDetails;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

public interface SharingDetailsRepository extends BaseRepository<SharingDetails, String> {

    SharingDetails findOneByTransactionIdAndSharingType(String tid, Integer type);

    List<SharingDetails> findOneByTransactionIdInAndSharingType(List<String> transactionIds, Integer type);

    List<SharingDetails> findAllByDataStatusAndIdInAndSharingType(Integer save, List<String> sharingDetailIds, Integer sharingType);
}
