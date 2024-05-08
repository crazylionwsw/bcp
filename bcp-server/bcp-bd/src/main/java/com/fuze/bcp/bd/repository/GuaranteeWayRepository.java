package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.GuaranteeWay;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by LB on 2016-10-26.
 */
public interface GuaranteeWayRepository extends BaseDataRepository<GuaranteeWay,String> {

    Page<GuaranteeWay> findAllByOrderByCodeAsc(Pageable pageable);
}
