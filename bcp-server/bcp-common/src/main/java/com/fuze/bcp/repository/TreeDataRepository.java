package com.fuze.bcp.repository;

import com.fuze.bcp.domain.TreeDataEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 基础数据基类的存储基类
 */
public interface TreeDataRepository<T extends TreeDataEntity,ID extends Serializable> extends BaseDataRepository<T,ID> {

    List<T> findByParentIdOrderByTsAsc(String parentId);


    List<T> findByParentIdAndDataStatus(String parentId, Integer ds);


}