package com.fuze.bcp.cardealer.repository;

import com.fuze.bcp.cardealer.domain.DealerGroup;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DealerGroupRepository extends BaseDataRepository<DealerGroup,String> {

    Page<DealerGroup> findAllByOrderByCodeAsc(Pageable pageable);

    Page<DealerGroup> findAllByOrderByTsDesc(Pageable pageable);
}
