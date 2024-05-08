package com.fuze.bcp.service;

import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.domain.MongoBaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by admin on 2017/5/24.
 */
public interface IBaseService<T extends MongoBaseEntity> {

    /**
     * 获取单个
     * @param id
     * @return
     */
    T getOne(String id);

    /**
     * 获取一条可用的数据
     * @param id
     * @return
     */
    T getAvailableOne(String id);

    /**
     * 获取一条可用的数据
     * @param sort
     * @return
     */
    T getAvaliableOneSortBy(Sort sort);

    /**
     * 获取列表
     * @return
     */
    List<T> getAll();
    List<T> getAll(Sort sort);

    /**
     * 获取可用的数据列表（DataStatus = SAVE)
     * @return
     */
    List<T> getAvaliableAll();

    /**
     * 获取分页列表
     * @return
     */
    Page<T> getAll(Integer currentPage);

    Page<T> getAvaliableAll(Integer currentPage);

    /**
     * 获取分页列表.定制分页信息
     * @return
     */
    Page<T> getAll(Pageable pageable);

    Page<T> getAvaliableAll(Pageable pageable);

    /**
     * 获取多个
     * @param ids
     * @return
     */
    List<T> getAvaliableList(List<String> ids);

    /**
     * 查询
     * @param q
     * @return
     */
    Page<T> search(String q);

    /**
     * 暂存
     * @param t
     * @return
     */
    T tempSave(T t);

    /**
     * 废弃/删除
     * @param id
     * @return
     */
    T delete(String id);

    /**
     * 废弃一条数据
     * @param id
     * @return
     */
    T discard(String id);

    /**
     * 物理删除
     * @param id
     * @return
     */
    T deleteReal(String id);

    /**
     * 批量删除（直接删除）
     * @param ts
     */
    void deleteReal(List<T> ts);

    /**
     * 批量删除
     * @param ids
     */
    void deleteRealByIds(List<String> ids);

    /**
     * 保存
     * @param t
     * @return
     */
    T save(T t);

    /**
     * 批量保存
     * @param ts
     * @return
     */
    List<T> save(List<T> ts);

    List<String> getSearchTransactionIds(SearchBean searchBean,int stageNum);

    List<T> getAvaliableAll(Sort sort);
}
