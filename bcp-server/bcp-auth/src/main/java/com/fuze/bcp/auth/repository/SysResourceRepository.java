package com.fuze.bcp.auth.repository;


import com.fuze.bcp.auth.domain.SysResource;
import com.fuze.bcp.repository.TreeDataRepository;

import java.util.List;

/**
 *
 * Created by Gumenghao on 2016/10/20.
 */
public interface SysResourceRepository extends TreeDataRepository<SysResource,String> {

    List<SysResource> findByParentIdIsNull();

    List<SysResource> findByParentIdOrderByCodeAsc(String parentId);

}