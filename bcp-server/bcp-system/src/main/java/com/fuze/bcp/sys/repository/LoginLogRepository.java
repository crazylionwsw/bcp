package com.fuze.bcp.sys.repository;

import com.fuze.bcp.repository.BaseRepository;
import com.fuze.bcp.sys.domain.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 系统日志
 */

public interface LoginLogRepository extends BaseRepository<LoginLog, String> {

}
