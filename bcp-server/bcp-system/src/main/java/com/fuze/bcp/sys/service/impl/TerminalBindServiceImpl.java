package com.fuze.bcp.sys.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.sys.domain.TerminalBind;
import com.fuze.bcp.sys.repository.TerminalBindRepository;
import com.fuze.bcp.sys.service.ITerminalBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class TerminalBindServiceImpl extends BaseServiceImpl<TerminalBind, TerminalBindRepository> implements ITerminalBindService {



    @Autowired
    TerminalBindRepository terminalBindRepository;

    @Override
    public TerminalBind getOneByDataStatusAndLoginUserId(String userId) {
        return terminalBindRepository.findByDataStatusAndLoginUserId(DataStatus.SAVE, userId);
    }

    @Override
    public TerminalBind getOneByDataStatusAndTerminalDevice(String terminalDeviceId) {
        return terminalBindRepository.findByDataStatusAndTerminalDeviceId(DataStatus.SAVE, terminalDeviceId);
    }

    @Override
    public Page<TerminalBind> getTerminalBindByIds(Integer currentPage, List<String> userIds) {
        PageRequest pr = new PageRequest(currentPage, 20);
        return terminalBindRepository.findByLoginUserIdIn(pr,userIds);
    }


}
