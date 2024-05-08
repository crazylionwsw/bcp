package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.CarModel;
import com.fuze.bcp.bd.repository.CarModelRepository;
import com.fuze.bcp.bd.service.ICarModelService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/6/12.
 */
@Service
public class CarModelServiceImpl extends BaseDataServiceImpl<CarModel, CarModelRepository> implements ICarModelService {

    @Autowired
    CarModelRepository carModelRepository;

    @Override
    public Page<CarModel> getAllByCarBrand(String carBrandId, Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20);
        return carModelRepository.findByCarBrandId(carBrandId, pr);
    }

    @Override
    public List<CarModel> getAllByCarBrand(String carBrandId,Sort sort) {
        return carModelRepository.findByCarBrandId(carBrandId,sort);
    }

    @Override
    public List<CarModel> getLookupsByCarBrand(String carBrandId) {
        return carModelRepository.findByDataStatusAndCarBrandId(DataStatus.SAVE, carBrandId);
    }

    @Override
    public CarModel getOneByRefId(String id) {
        return carModelRepository.findOneByRefModelId(id);
    }
}
