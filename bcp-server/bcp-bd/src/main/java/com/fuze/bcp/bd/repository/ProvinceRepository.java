package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.Province;
import com.fuze.bcp.repository.TreeDataRepository;

import java.util.List;

public interface ProvinceRepository extends TreeDataRepository<Province,String> {

    List<Province> findByDataStatusAndParentId(Integer ds, String provinceId);

    List<Province> findByDataStatusAndParentIdIsNull(Integer ds);

    List<Province> findByParentIdIsNull();

}
