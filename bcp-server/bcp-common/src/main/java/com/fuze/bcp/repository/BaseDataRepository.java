package com.fuze.bcp.repository;

import com.fuze.bcp.domain.BaseDataEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

/**
 * 基础数据基类的存储基类
 */
public interface BaseDataRepository<T extends BaseDataEntity,ID extends Serializable> extends BaseRepository<T,ID> {

    List<T> findByDataStatus(Integer ds);

    Page<T> findByName(String name, Pageable pageable);

    T findByName(String name);

    T findOneByCode(String code);

    Page<T> findByNameLike(String name, Pageable pageable);

    List<T> findByNameLike(String name);

    Page<T> findByDataStatusAndNameLike(Integer ds, String name, Pageable pageable);

    List<T> findByDataStatusAndNameLike(Integer ds, String name);

    List<T> findByDataStatusAndCodeIn(Integer save, List<String> codes);
}