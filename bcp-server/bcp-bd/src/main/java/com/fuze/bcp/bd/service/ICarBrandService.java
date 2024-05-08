package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.CarBrand;
import com.fuze.bcp.service.IBaseDataService;

/**
 * Created by admin on 2017/6/2.
 */
public interface ICarBrandService extends IBaseDataService<CarBrand> {

    CarBrand getOneByRefId(String refMakeId);
}
