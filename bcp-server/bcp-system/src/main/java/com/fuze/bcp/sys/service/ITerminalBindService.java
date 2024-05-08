package com.fuze.bcp.sys.service;

import com.fuze.bcp.service.IBaseService;
import com.fuze.bcp.sys.domain.APKRelease;
import com.fuze.bcp.sys.domain.TerminalBind;
import com.fuze.bcp.sys.domain.TerminalDevice;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by lenovo on 2017-06-13.
 */
public interface ITerminalBindService extends IBaseService<TerminalBind>{

    TerminalBind getOneByDataStatusAndLoginUserId(String userId);

    TerminalBind getOneByDataStatusAndTerminalDevice(String terminalDeviceId);

    Page<TerminalBind> getTerminalBindByIds(Integer currentPage,List<String> userIds);


}
