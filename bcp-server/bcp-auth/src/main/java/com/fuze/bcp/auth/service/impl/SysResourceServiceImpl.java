package com.fuze.bcp.auth.service.impl;

import com.fuze.bcp.auth.domain.SysResource;
import com.fuze.bcp.auth.repository.SysResourceRepository;
import com.fuze.bcp.auth.service.ISysResourceService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.service.impl.TreeDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/27.
 */
@Service
public class SysResourceServiceImpl extends TreeDataServiceImpl<SysResource, SysResourceRepository> implements ISysResourceService {

    @Autowired
    SysResourceRepository sysResourceRepository;


    @Override
    public List<SysResource> getChildrenOrderByCode(String parentId) {

        return sysResourceRepository.findByParentIdOrderByCodeAsc(parentId);
    }
}
