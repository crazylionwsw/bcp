package com.fuze.bcp.bd.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 4S 店经销商员工信息
 * Created by sean on 16/10/10.
 */
@Document(collection = "bd_dealeremployee")
public class DealerEmployee extends Employee {

    /**
     * 所属组织机构
     */
    private String carDealerId;

    private String roles = null;

    /**
     * 员工职位 0 表示法人，1 表示 销售经理， 2 表示销售人员
     */
    private String career;

    public String getCarDealerId() {
        return carDealerId;
    }

    public void setCarDealerId(String carDealerId) {
        this.carDealerId = carDealerId;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }
}
