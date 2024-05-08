package com.fuze.bcp.sys.service.impl;

import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.sys.domain.LoginLog;
import com.fuze.bcp.sys.repository.LoginLogRepository;
import com.fuze.bcp.sys.service.ILoginLogService;
import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLog, LoginLogRepository> implements ILoginLogService{
}
