package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * Created by CJ on 2017/6/16.
 */
@Data
public class EmployeeLookupBean extends APIBaseBean {


    private String username;


    private String cell;


    private String orgInfoId;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getOrgInfoId() {
        return orgInfoId;
    }

    public void setOrgInfoId(String orgInfoId) {
        this.orgInfoId = orgInfoId;
    }

}
