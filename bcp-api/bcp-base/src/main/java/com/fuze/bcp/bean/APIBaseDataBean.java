package com.fuze.bcp.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 基础数据
 */
@Data
public class APIBaseDataBean extends APIBaseBean {

    /**
     * 编码
     */
    @NotNull
    private String code;


    /**
     * 名称
     */
    @NotNull
    private String name;


}
