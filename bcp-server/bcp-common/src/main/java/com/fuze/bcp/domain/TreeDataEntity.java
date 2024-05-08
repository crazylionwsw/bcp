package com.fuze.bcp.domain;

import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * 树形数据基类
 * Created by sean on 2016/10/20.
 */
@Data
public class TreeDataEntity extends BaseDataEntity {

    /**
     * 上级信息
     */
    private String parentId = null;

    /**
     * 物化路径
     */
    @Transient
    private String path = null;

    /**
     * 节点的级别
     */
    @Transient
    private Integer depth = 0;

}
