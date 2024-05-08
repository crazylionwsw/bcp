package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.CarBrand;
import com.fuze.bcp.bd.repository.CarBrandRepository;
import com.fuze.bcp.bd.service.ICarBrandService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/6/12.
 */
@Service
public class CarBrandServiceImpl extends BaseDataServiceImpl<CarBrand, CarBrandRepository> implements ICarBrandService{

    @Autowired
    CarBrandRepository carBrandRepository;

    @Override
    public CarBrand getOneByRefId(String refMakeId) {
        return carBrandRepository.findOneByRefMakeId(refMakeId);
    }
}
