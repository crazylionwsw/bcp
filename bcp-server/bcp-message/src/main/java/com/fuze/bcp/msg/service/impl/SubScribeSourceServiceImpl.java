package com.fuze.bcp.msg.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.msg.domain.SubSribeSource;
import com.fuze.bcp.msg.repository.SubSribeSourceRepository;
import com.fuze.bcp.msg.service.ISubScribeSourceService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/9/14.
 */
@Service
public class SubScribeSourceServiceImpl extends BaseServiceImpl<SubSribeSource, SubSribeSourceRepository> implements ISubScribeSourceService {
    @Override
    public List<SubSribeSource> getByGroupName(String groupName) {
        return baseRepository.findAllByDataStatusAndGroupName(DataStatus.SAVE, groupName);
    }
}
