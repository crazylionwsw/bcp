package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.CreditPhotograph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by Lily on 2017/7/18.
 */
public interface CreditPhotographRepository extends BaseBillRepository<CreditPhotograph,String> {
    Page<CreditPhotograph> findAllByUploadFinishOrderByTsDesc(Boolean uploadFinish, Pageable pr);

    Page<CreditPhotograph> findAllByUploadFinishAndCustomerIdIn(Boolean uploadFinish, List<String> customerIds, Pageable pr);

    CreditPhotograph findByCustomerId(String customerId);

    Page<CreditPhotograph> findAllByCashSourceIdAndUploadFinish(String cashSourceId, Boolean b, Pageable pr);

    List<CreditPhotograph> findAllByCashSourceIdAndUploadFinish(String cashSourceId,Boolean b,Sort sort);
}
