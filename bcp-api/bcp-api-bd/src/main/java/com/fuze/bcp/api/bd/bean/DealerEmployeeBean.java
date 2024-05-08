package com.fuze.bcp.api.bd.bean;

import lombok.Data;

/**
 * 4S 店经销商员工信息
 */
@Data
public class DealerEmployeeBean extends EmployeeBean {

    /**
     * 所属组织机构
     */
    private String carDealerId;
    /**
     * 所属组织机构名称
     */
    private String carDealerName;
    /**
     * 所属组织机构名称
     */
    private String carDealerAddress;

    private String roles = null;

    /**
     * 员工职位 0 表示法人，1 表示 销售经理， 2 表示销售人员
     */
    private String career;

}
