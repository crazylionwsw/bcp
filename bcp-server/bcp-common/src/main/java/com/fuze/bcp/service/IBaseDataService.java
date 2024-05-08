package com.fuze.bcp.service;

import com.fuze.bcp.domain.BaseDataEntity;

import java.util.List;

/**
 * Created by admin on 2017/5/24.
 */
public interface IBaseDataService<T extends BaseDataEntity> extends IBaseService<T> {

    /**
     * 获取单个
     * @param code
     * @return
     */
    T getOneByCode(String code);

    /**
     * 通过名称模糊查询
     * @param name
     * @return
     */
    List<T> searchByName(String name);

    /**
     * 获取多个
     * @param codes
     * @return
     */
    List<T> getAvaliableListByCodes(List<String> codes);
}
