package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.bean.APIBaseBean;

/**
 * Created by Lily on 2017/7/28.
 */
public class CreditCustomerBean extends APIBaseBean {

    private String name;

    private String identifyNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifyNo() {
        return identifyNo;
    }

    public void setIdentifyNo(String identifyNo) {
        this.identifyNo = identifyNo;
    }
}
