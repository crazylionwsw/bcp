package com.fuze.bcp.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 费率类型
 */
@Data
public class RateType implements Serializable {

    /**
     * 期数（月份）
     */
    private Integer  months;

    /**
     * 总费率
     */
    private Double ratio;

}
