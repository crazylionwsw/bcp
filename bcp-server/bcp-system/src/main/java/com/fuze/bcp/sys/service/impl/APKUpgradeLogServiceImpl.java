package com.fuze.bcp.sys.service.impl;

import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.sys.domain.APKUpgradeLog;
import com.fuze.bcp.sys.repository.APKUpgradeLogRepository;
import com.fuze.bcp.sys.service.IAPKUpgradeLogService;
import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class APKUpgradeLogServiceImpl extends BaseServiceImpl<APKUpgradeLog, APKUpgradeLogRepository> implements IAPKUpgradeLogService {
}
