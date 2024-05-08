package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.BillType;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by LB on 2016-10-26.
 */
public interface BillTypeRepository extends BaseDataRepository<BillType,String> {

    Page<BillType> findAllByOrderByCodeAsc(Pageable pageable);
}
