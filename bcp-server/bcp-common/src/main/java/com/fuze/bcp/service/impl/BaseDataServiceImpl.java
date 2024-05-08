package com.fuze.bcp.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.domain.BaseDataEntity;
import com.fuze.bcp.repository.BaseDataRepository;
import com.fuze.bcp.service.IBaseDataService;

import java.util.List;

/**
 * Created by admin on 2017/6/2.
 */
public class BaseDataServiceImpl<T extends BaseDataEntity, R extends BaseDataRepository<T, String>> extends BaseServiceImpl<T, R> implements IBaseDataService<T> {

    @Override
    public T getOneByCode(String code) {
        return baseRepository.findOneByCode(code);
    }

    @Override
    public List<T> searchByName(String name) {
        return null;
    }

    @Override
    public List<T> getAvaliableListByCodes(List<String> codes) {
        return baseRepository.findByDataStatusAndCodeIn(DataStatus.SAVE, codes);
    }
}
