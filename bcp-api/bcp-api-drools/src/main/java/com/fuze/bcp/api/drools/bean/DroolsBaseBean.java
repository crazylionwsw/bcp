package com.fuze.bcp.api.drools.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ on 2017/8/7.
 */
@Data
public class DroolsBaseBean<T extends APIBaseBean> implements Serializable {

    T apiBaseBean;

    List<String> message = new ArrayList<String>();
}
