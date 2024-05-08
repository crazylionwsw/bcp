package com.fuze.bcp.sys.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.sys.domain.TerminalDevice;
import com.fuze.bcp.sys.repository.TerminalDeviceRepository;
import com.fuze.bcp.sys.service.ITerminalDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class TerminalDeviceServiceImpl  extends BaseServiceImpl<TerminalDevice, TerminalDeviceRepository> implements ITerminalDeviceService{
    @Autowired
    TerminalDeviceRepository terminalDeviceRepository;

    @Override
    public TerminalDevice getOneByDataStatusAndIdentify(String identify) {
        return terminalDeviceRepository.findOneByIdentifyAndDataStatus(identify, DataStatus.SAVE);
    }
}
