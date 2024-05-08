package com.fuze.bcp.repository;

import com.fuze.bcp.domain.MongoBaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by sean on 2016/11/23.
 */
public interface BaseRepository<T extends MongoBaseEntity, ID extends Serializable> extends MongoRepository<T, ID> {

    /**
     * 获取数据，带分页
     *
     * @param ds
     * @param pageable
     * @return
     */
    Page<T> findByDataStatus(Integer ds, Pageable pageable);

    /**
     * 获取所有的数据，不带分页
     *
     * @param ds
     * @return
     */
    List<T> findAllByDataStatus(Integer ds);

    /**
     * 获取所有的数据
     *
     * @param ds
     * @return
     */
    List<T> findAllByDataStatus(Integer ds, Sort sort);

    /**
     * 分页获取所有的数据
     * @param ds
     * @return
     */
    Page<T> findAllByDataStatus(Integer ds, Pageable pageable);


    /**
     * 通过ids查询
     *
     * @param ds
     * @param ids
     * @return
     */
    List<T> findByDataStatusAndIdIn(Integer ds, List<String> ids);

    Page<T> findByDataStatusAndIdIn(Integer ds, List<String> ids, Pageable pageable);


    /**
     * 获取所有，按时间排序
     *
     * @return
     */
    Page<T> findAllByOrderByTsDesc(Pageable pageable);

    /**
     * 获取一条可用记录
     * @param ds
     * @param sort
     * @return
     */
    T findOneByDataStatus(Integer ds, Sort sort);


    <T extends MongoBaseEntity> T findOneByDataStatusAndId(Integer save, String id);





}
