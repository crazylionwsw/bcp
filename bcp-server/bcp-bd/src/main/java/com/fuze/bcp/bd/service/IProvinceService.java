package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.Province;
import com.fuze.bcp.service.ITreeDataService;

import java.util.List;

/**
 * Created by user on 2017/6/15.
 */
public interface IProvinceService extends ITreeDataService<Province> {

    List<Province> getByParentIdIsNull();

}
