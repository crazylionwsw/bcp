package com.fuze.bcp.api.auth.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录用户角色
 * Created by sean on 2017/5/19.
 */
@Data
@MongoEntity(entityName = "au_sysrole")
public class SysRoleBean extends APIBaseDataBean {

    /**
     * 系统资源的ID信息
     */
    private List<String> sysResourceIds = new ArrayList<String>();

}
