package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.CarBrand;
import com.fuze.bcp.bd.domain.CarModel;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by zxp on 2017/3/11.
 */
public interface CarModelRepository extends BaseDataRepository<CarModel, String> {

    List<CarModel> findByDataStatusAndCarBrandId(Integer save, String carBrandId);

    List<CarModel> findAllByDataStatusAndCarBrandId(Integer save, String carBrandId, Sort name);

    CarModel findOneByRefModelId(String refModelId);

    List<CarModel> findByCarBrandId(String carBrandId);

    List<CarModel> findByCarBrandId(String carBrandId,Sort sort);
    Page<CarModel> findByCarBrandId(String carBrandId, Pageable page);
}
