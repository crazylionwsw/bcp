package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 角色的影像操作权限
 */
@Data
public class RoleControl implements Serializable {

    /**
     * 角色
     */
    private String roleId = null;

    /**
     * 权限列表
     */
    private String  controlls;
}
