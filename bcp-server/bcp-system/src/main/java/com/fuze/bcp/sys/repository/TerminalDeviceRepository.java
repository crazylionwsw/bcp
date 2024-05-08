package com.fuze.bcp.sys.repository;

import com.fuze.bcp.repository.BaseRepository;
import com.fuze.bcp.sys.domain.TerminalDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Administrator on 2016/10/21.
 */
public interface TerminalDeviceRepository extends BaseRepository<TerminalDevice, String> {

    TerminalDevice findOneByIdentifyAndDataStatus(String identify, Integer ds);
}
