package com.fuze.bcp.api.creditcar.bean.declaration;

/**
 * Created by hecaifeng on 2017/5/25.
 */

import lombok.Data;

import java.io.Serializable;

/**
 * 账户收入流水
 */
@Data
public class IncomeAccount implements Serializable{

    /**
     * 日期 年
     */
    private String year;
    /**
     * 日期 月
     */
    private String month;
    /**
     * 收入（万元）
     */
    private Double income;

}
