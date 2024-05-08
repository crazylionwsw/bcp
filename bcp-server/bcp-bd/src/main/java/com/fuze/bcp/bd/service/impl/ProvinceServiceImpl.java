package com.fuze.bcp.bd.service.impl;


import com.fuze.bcp.bd.domain.Province;
import com.fuze.bcp.bd.repository.ProvinceRepository;
import com.fuze.bcp.bd.service.IProvinceService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import com.fuze.bcp.service.impl.TreeDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2017/6/15.
 */
@Service
public class ProvinceServiceImpl extends TreeDataServiceImpl<Province,ProvinceRepository> implements IProvinceService{

    @Autowired
    ProvinceRepository provinceRepository;


    @Override
    public List<Province> getByParentIdIsNull() {
        return provinceRepository.findByParentIdIsNull();
    }
}
