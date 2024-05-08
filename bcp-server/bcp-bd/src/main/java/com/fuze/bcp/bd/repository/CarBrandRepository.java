package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.CarBrand;
import com.fuze.bcp.repository.BaseDataRepository;

/**
 * Created by xinjing on 2016/10/20.
 */
public interface CarBrandRepository extends BaseDataRepository<CarBrand,String> {

    CarBrand findOneByRefMakeId(String makeId);

}
