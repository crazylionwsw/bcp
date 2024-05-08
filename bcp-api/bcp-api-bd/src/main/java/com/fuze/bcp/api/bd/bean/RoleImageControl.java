package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APIBaseBean;

import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 角色的影像操作权限
 * Created by sean on 2016/10/20.
 */
@Data
public class RoleImageControl extends APIBaseDataBean {

    private String roleId = null;

    /**
     * 权限列表
     *      档案类型ID，RWD
     *      档案类型ID，RWD
     */
    private Map<String,String>  imageTypesControlls = new HashMap<String,String>();
}
