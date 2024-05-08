package com.fuze.bcp.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.domain.TreeDataEntity;
import com.fuze.bcp.repository.TreeDataRepository;
import com.fuze.bcp.service.ITreeDataService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/2.
 */
public class TreeDataServiceImpl<T extends TreeDataEntity, R extends TreeDataRepository<T, String>> extends BaseDataServiceImpl<T, R> implements ITreeDataService<T> {


    @Override
    public List<T> getChildren(String parentId) {
        return baseRepository.findByParentIdOrderByTsAsc(parentId);
    }

    @Override
    public List<T> getAvailableChildren(String parentId) {
        return baseRepository.findByParentIdAndDataStatus(parentId, DataStatus.SAVE);
    }

    @Override
    public List<T> getLookups(T parent, Integer depth) {
        List<T> treeData = new ArrayList<T>();

        String parentId = parent == null ? "0" : parent.getId();
        List<T> data = this.getAvailableChildren(parentId);

        if (data != null || data.size() > 0) {
            for (T t : data) {
                //级别
                t.setDepth(depth);

                //物化路径
                String path = parent == null ? t.getName() : parent.getPath() + " > " + t.getName();
                t.setPath(path);

                treeData.add(t);

                //递归
                treeData.addAll(getLookups(t, depth + 1));
            }
        }

        return treeData;
    }
}
