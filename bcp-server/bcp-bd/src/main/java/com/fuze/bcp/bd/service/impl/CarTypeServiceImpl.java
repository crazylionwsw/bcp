package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.CarModel;
import com.fuze.bcp.bd.domain.CarType;
import com.fuze.bcp.bd.repository.CarTypeRepository;
import com.fuze.bcp.bd.service.ICarTypeService;
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
public class CarTypeServiceImpl extends BaseDataServiceImpl<CarType, CarTypeRepository> implements ICarTypeService {

    @Autowired
    CarTypeRepository carTypeRepository;

    @Override
    public List<CarType>getAllByCarModel(String carModelId,Sort sort) {
        return carTypeRepository.findAllByCarModelId(carModelId,sort);
    }

    @Override
    public List<CarType> getAllByCarModels(List<String> carModelIds) {
        return carTypeRepository.findByDataStatusAndCarModelIdIn(DataStatus.SAVE, carModelIds);
    }

    @Override
    public Page<CarType> getAllByCarModel(String carModelId, Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20);
        return carTypeRepository.findAllByCarModelId(carModelId, pr);
    }

    @Override
    public Page<CarType> getAllByCarBrand(String carBrandId, Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20);
        return carTypeRepository.findAllByCarBrandId(carBrandId, pr);
    }

    @Override
    public List<CarType> getAllByCarBrand(String carBrandId) {
        return carTypeRepository.findAllByCarBrandId(carBrandId);
    }

    @Override
    public CarType getOneByRefId(String id) {
        return carTypeRepository.findOneByRefStyleId(id);
    }
}
