package com.fuze.bcp.api.workflow.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 定时任务记录
 * Created by CJ on 2017/7/3.
 */
@Data
public class GroupBean extends APIBaseBean {

    private String name;

    private String type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
