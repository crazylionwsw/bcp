package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.SourceRate;
import com.fuze.bcp.service.IBaseDataService;

import java.util.List;

/**
 * Created by CJ on 2017/6/14.
 */
public interface ISourceRateService extends IBaseDataService<SourceRate> {

    List<SourceRate> getByCashSourceId(String cashSourceId);
}
