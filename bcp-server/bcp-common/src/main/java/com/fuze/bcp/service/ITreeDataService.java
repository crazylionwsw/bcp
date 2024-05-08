package com.fuze.bcp.service;

import com.fuze.bcp.domain.BaseDataEntity;

import java.util.List;

/**
 * Created by admin on 2017/5/24.
 */
public interface ITreeDataService<T extends BaseDataEntity> extends IBaseDataService<T> {

    /**
     * 获取下级数据（全部）
     * @param parentId
     * @return
     */
    List<T> getChildren(String parentId);

    /**
     * 获取下级数据（可用的）
     * @param parentId
     * @return
     */
    List<T> getAvailableChildren(String parentId);


    /**
     * 获取可用的层级数据
     * @param parent
     * @param depth
     * @return
     */
    List<T> getLookups(T parent, Integer depth);
}
