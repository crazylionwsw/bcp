package com.fuze.bcp.sys.service;

import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.IBaseDataService;
import com.fuze.bcp.sys.domain.SysParam;

import java.util.Map;

/**
 * Created by admin on 2017/6/2.
 */
public interface ISysParamService extends IBaseDataService<SysParam> {

    //根据编码得到利率
    ResultBean<Map<?,?>> getMap(String businessTypeCode);
}
