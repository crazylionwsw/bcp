package com.fuze.bcp.api.customer.bean;

import lombok.Data;

import java.io.Serializable;

/**
 *公司基本信息
 */
@Data
public class CustomerJobBean implements Serializable {
    /**
     * 公司名称
     */
    private String companyName = null;

    /**
     * 公司地址
     */
    private String companyAddress = null;

    /**
     * 公司性质
     */
    private String companyNature = null;

    /**
     * 职位
     */
    private String job = null;

    /**
     * 学历
     */
    private String education = null;

    /**
     * 入职日期
     */
    private String entryDate = null;

    /**
     * 工作年限
     */
    private Integer workDate = null;

    /**
     * 年薪
     */
    private Double salary = 0.00;

    /**
     * 人事姓名
     */
    private String hrName = null;

    /**
     * 人事联系方式
     */
    private String hrCell = null;

    /**
     * 人事邮箱
     */
    private String email = null;

}
