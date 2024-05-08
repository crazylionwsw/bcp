package com.fuze.bcp.api.bd.service;

import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.ResultBean;

/**
 * Created by ${Liu} on 2018/3/23.
 */
public interface IValidateBizService {

    /**
     * 检测对象是否存在
     * @param baseBean
     * @return
     */
    public ResultBean<String> actCheckExist(APIBaseBean baseBean);

    /**
     * 根据属性检测对象是否存在
     * @param baseBean
     * @param propname
     * @param val
     * @return
     */
    public ResultBean<String> actCheckUnique(APIBaseBean baseBean, String propname, Object val);
}
