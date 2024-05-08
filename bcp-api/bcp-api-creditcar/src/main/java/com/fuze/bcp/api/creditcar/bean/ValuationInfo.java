package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by sean on 2017/4/24.
 */
@Data
public class ValuationInfo implements Serializable {

    /**
     * 评估来源的编码
     */
    private String evaluateSourceCode;

    /**
     * 估值提交日期
     */
    private String valuationDate = null;

    /**
     * 估值价格
     */
    private String  price = null;

    /**
     * 估值报告下载链接
     */
    private String reportUrl = null;

}
