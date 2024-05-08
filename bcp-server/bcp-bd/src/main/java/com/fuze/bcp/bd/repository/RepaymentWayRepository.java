package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.RepaymentWay;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by LB on 2016-10-26.
 */
public interface RepaymentWayRepository extends BaseDataRepository<RepaymentWay,String> {

    Page<RepaymentWay> findAllByOrderByCodeAsc(Pageable pageable);

}
