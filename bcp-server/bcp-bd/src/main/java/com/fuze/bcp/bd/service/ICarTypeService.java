package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.CarModel;
import com.fuze.bcp.bd.domain.CarType;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by admin on 2017/5/24.
 */
public interface ICarTypeService extends IBaseDataService<CarType> {

    List<CarType> getAllByCarModel(String carModelId,Sort sort);

    List<CarType> getAllByCarModels(List<String> carModelIds);

    Page<CarType> getAllByCarModel(String carModelId, Integer currentPage);

    Page<CarType> getAllByCarBrand(String carBrandId, Integer currentPage);

    List<CarType> getAllByCarBrand(String carBrandId);

    CarType getOneByRefId(String id);

}
