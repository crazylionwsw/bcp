package com.fuze.bcp.api.bd.bean;

import lombok.Data;

/**
 * Created by ${Liu} on 2018/3/6.
 */
@Data
public class FeeBillBean extends FeeValueBean{

    /**
     * 不选中为0   选中为1
     * 默认不选中
     */
    private Integer isChoose = 0;
}
