package com.fuze.bcp.bean;

import lombok.Data;

/**
 * Created by CJ on 2017/6/16.
 */
@Data
public class APITreeLookupBean extends APITreeDataBean{

    /**
     * 物化路径
     */
    private String path = null;

    /**
     * 节点的级别
     */
    private Integer depth = 0;
}
