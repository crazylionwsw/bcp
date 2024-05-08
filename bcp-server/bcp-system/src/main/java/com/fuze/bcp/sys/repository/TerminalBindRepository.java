package com.fuze.bcp.sys.repository;

import com.fuze.bcp.repository.BaseRepository;
import com.fuze.bcp.sys.domain.TerminalBind;
import com.fuze.bcp.sys.domain.TerminalDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public interface TerminalBindRepository extends BaseRepository<TerminalBind, String> {

    TerminalBind findByDataStatusAndLoginUserId(Integer save, String userId);

    TerminalBind findByDataStatusAndTerminalDeviceId(Integer save, String terminalDeviceId);

    Page<TerminalBind> findByDataStatusAndLoginUserIdLike(Integer ds, String loginUserId, Pageable pageable);

    Page<TerminalBind> findByLoginUserIdIn(Pageable pageable,List<String> loginUserIds);


}
