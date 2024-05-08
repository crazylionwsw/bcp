package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.SharingDetails;
import com.fuze.bcp.service.IBaseService;

import java.util.List;

/**
 * Created by Lily on 2017/9/23.
 */
public interface IDealerSharingDetailsService extends IBaseService<SharingDetails> {

    SharingDetails getByTransactionIdAndType(String transactionId, Integer type);

    List<SharingDetails> getByTransactionIdInAndType(List<String> transactionIds, Integer type);

    List findDistinctStatusByTransactionIdInAndSharingTypeAndPledgeDateReceiveTimeLike(List<String> transactionIds, Integer type, String month);

    List<SharingDetails> getAllByIdInAndType(List<String> sharingDetailIds, Integer sharingType);


//    List<DealerSharingDetails> getByDealerSharingIds(List<String> dealerSharingIds);
//
//    Page<DealerSharingDetails> getByDealerSharingIds(List<String> dealerSharingIds, Integer page);
//
//    List<DealerSharingDetails> getByDealerSharingId(String dealerSharingId);
}
