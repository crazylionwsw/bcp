package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * 角色的影像操作权限
 * Created by sean on 2016/10/20.
 */
@Document(collection = "bd_roleimagecontroll")
public class RoleImageControl extends BaseDataEntity {

    private String roleId = null;

    /**
     * 权限列表
     *      档案类型ID，RWD
     *      档案类型ID，RWD
     */
    private Map<String,String>  imageTypesControlls = new HashMap<String,String>();
}
