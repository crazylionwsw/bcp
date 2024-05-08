package com.fuze.bcp.msg.service;

import com.fuze.bcp.msg.domain.SubSribeSource;
import com.fuze.bcp.service.IBaseService;

import java.util.List;

/**
 * Created by CJ on 2017/9/14.
 */
public interface ISubScribeSourceService extends IBaseService<SubSribeSource> {
    List<SubSribeSource> getByGroupName(String groupName);
}
