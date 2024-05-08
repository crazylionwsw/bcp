package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.SourceRate;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by sean on 2016/11/29.
 */
public interface SourceRateRepository extends BaseDataRepository<SourceRate, String> {

    List<SourceRate> findAllByCashSourceId(String sourceTypeID, Sort sort);

}
