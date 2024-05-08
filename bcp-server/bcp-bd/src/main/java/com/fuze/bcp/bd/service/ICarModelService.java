package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.CarBrand;
import com.fuze.bcp.bd.domain.CarModel;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by admin on 2017/6/2.
 */
public interface ICarModelService extends IBaseDataService<CarModel> {

    Page<CarModel> getAllByCarBrand(String carBrandId, Integer currentPage);

    List<CarModel> getAllByCarBrand(String carBrandId,Sort sort);

    List<CarModel> getLookupsByCarBrand(String carBrandId);

    /**
     * 获取车系
     * @param id
     * @return
     */
    CarModel getOneByRefId(String id);


}
