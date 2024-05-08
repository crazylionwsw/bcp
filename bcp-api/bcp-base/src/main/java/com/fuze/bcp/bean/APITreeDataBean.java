package com.fuze.bcp.bean;

import lombok.Data;

/**
 * 树型数据基类
 */
@Data
public class APITreeDataBean extends APIBaseDataBean {


    /**
     * 上级信息
     */
    private String parentId = null;

}
