package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.SourceRate;
import com.fuze.bcp.bd.repository.SourceRateRepository;
import com.fuze.bcp.bd.service.ISourceRateService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/6/14.
 */
@Service
public class SourceRateServiceImpl extends BaseDataServiceImpl<SourceRate, SourceRateRepository> implements ISourceRateService{


    @Autowired
    SourceRateRepository sourceRateRepository;

    @Override
    public List<SourceRate> getByCashSourceId(String cashSourceId) {
        return sourceRateRepository.findAllByCashSourceId(cashSourceId, SourceRate.getTsSort());
    }
}
