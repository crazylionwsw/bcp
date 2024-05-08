package com.fuze.bcp.api.mq.bean;

import com.fuze.bcp.bean.APILookupBean;

/**
 * Created by ${Liu} on 2017/8/1.
 */
public class TaskDescribeLookupBean extends APILookupBean {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
