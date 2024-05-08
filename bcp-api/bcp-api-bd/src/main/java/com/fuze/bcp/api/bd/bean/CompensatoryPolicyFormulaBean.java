package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.Map;

/**
 * Created by CJ on 2018/1/15.
 */
@Data
public class CompensatoryPolicyFormulaBean extends APIBaseDataBean {

    private String formulaTemplate;

    private Map templateMap;

}
