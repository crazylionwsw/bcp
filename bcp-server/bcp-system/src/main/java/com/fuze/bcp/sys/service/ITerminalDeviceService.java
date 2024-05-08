package com.fuze.bcp.sys.service;

import com.fuze.bcp.service.IBaseService;
import com.fuze.bcp.sys.domain.APKRelease;
import com.fuze.bcp.sys.domain.TerminalDevice;

/**
 * Created by lenovo on 2017-06-13.
 */
public interface ITerminalDeviceService extends IBaseService<TerminalDevice>{
    TerminalDevice getOneByDataStatusAndIdentify(String identify);
}
