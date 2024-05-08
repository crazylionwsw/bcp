package com.fuze.bcp.msg.repository;

import com.fuze.bcp.msg.domain.MessageLog;
import com.fuze.bcp.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by CJ on 2017/7/27.
 */
public interface MessageLogRepository extends BaseRepository<MessageLog, String> {
}
