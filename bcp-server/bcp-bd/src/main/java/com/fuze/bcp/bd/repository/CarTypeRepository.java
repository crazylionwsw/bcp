package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.CarBrand;
import com.fuze.bcp.bd.domain.CarModel;
import com.fuze.bcp.bd.domain.CarType;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CarTypeRepository extends BaseDataRepository<CarType,String> {

    List<CarType> findAllByDataStatusAndCarBrandId(Integer save, String carBrandId, Sort name);

    List<CarType> findAllByCarBrandId(String carBrandId, Sort name);

    List<CarType> findAllByCarBrandId(String carBrandId);

    Page<CarType> findAllByCarBrandId(String carBrandId, Pageable page);

    /**
     * 通过品牌查询车型
     * @param save
     * @param carBrandIds
     * @return
     */
    List<CarType> findAllByDataStatusAndCarBrandIdIn(Integer save, List<String> carBrandIds);


    List<CarType> findAllByDataStatusAndCarModelId(Integer save, String carModelId, Sort name);

    /**
     * 通过车型Id 查询详情
     * @param save
     * @param id
     * @return
     */
    CarType findOneByDataStatusAndId(Integer save, String id);

    List<CarType> findAllByDataStatusAndCarModelId(Integer save, String carModelId);

    Page<CarType> findAllByCarModelId(String carModelId, Pageable page);

    List<CarType> findAllByCarModelId(String carModelId);
    List<CarType> findAllByCarModelId(String carModelId,Sort sort);

    List<CarType> findByDataStatusAndCarModelIdIn(Integer save, List<String> carModelIds);

    CarType findOneByRefStyleId(String refId);
}
