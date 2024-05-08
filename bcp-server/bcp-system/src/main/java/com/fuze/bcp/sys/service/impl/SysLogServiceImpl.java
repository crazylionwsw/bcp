package com.fuze.bcp.sys.service.impl;

import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.sys.domain.SysLog;
import com.fuze.bcp.sys.repository.SysLogRepository;
import com.fuze.bcp.sys.service.ISysLogService;
import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLog, SysLogRepository> implements ISysLogService {
}
