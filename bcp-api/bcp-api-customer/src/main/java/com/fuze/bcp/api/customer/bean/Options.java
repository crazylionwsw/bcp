package com.fuze.bcp.api.customer.bean;

/**
 * Created by ${Liu} on 2017/9/5.
 */

import lombok.Data;

import java.io.Serializable;

/**
 * 答案
 */
@Data
public class Options implements Serializable{

    /**
     * 答案内容
     */
    private String content;


    /**
     * 显示顺序
     */
    private int displayOrder;
}
