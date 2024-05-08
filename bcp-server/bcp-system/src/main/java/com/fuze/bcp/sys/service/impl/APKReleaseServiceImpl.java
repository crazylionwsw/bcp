package com.fuze.bcp.sys.service.impl;

import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.sys.domain.APKRelease;
import com.fuze.bcp.sys.repository.APKReleaseRepository;
import com.fuze.bcp.sys.service.IAPKReleaseService;
import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class APKReleaseServiceImpl extends BaseServiceImpl<APKRelease, APKReleaseRepository> implements IAPKReleaseService{
}
