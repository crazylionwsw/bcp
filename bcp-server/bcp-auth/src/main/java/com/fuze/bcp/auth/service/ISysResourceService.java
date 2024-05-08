package com.fuze.bcp.auth.service;

import com.fuze.bcp.auth.domain.SysResource;
import com.fuze.bcp.service.ITreeDataService;

import java.util.List;

/**
 * 系统权限维护服务
 * Created by admin on 2017/5/27.
 */
public interface ISysResourceService extends ITreeDataService<SysResource> {

    List<SysResource> getChildrenOrderByCode(String parentId);

}
