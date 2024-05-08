package com.fuze.bcp.msg.repository;

import com.fuze.bcp.msg.domain.Notice;
import com.fuze.bcp.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by CJ on 2017/10/30.
 */
public interface NoticeRepository extends BaseRepository<Notice, String> {
    Page<Notice> findByDataStatusAndStatusAndTypeAndSendTypeAndLoginUserNamesContaining(Integer save, int i, String type, String sendType, String loginUserName, Pageable pageable);

    Page<Notice> findByDataStatusAndStatusAndTypeAndSendType(Integer save, int i, String type, String sendType, Pageable pageable);
}
