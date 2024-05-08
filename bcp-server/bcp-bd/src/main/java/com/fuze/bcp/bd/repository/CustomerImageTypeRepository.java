package com.fuze.bcp.bd.repository;


import com.fuze.bcp.bd.domain.CustomerImageType;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 档案类型存储接口
 */
public interface CustomerImageTypeRepository extends BaseDataRepository<CustomerImageType,String> {

    List<CustomerImageType> findAllByOrderByCodeAsc();

}
