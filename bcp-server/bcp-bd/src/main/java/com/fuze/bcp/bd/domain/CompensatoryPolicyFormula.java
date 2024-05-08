package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by CJ on 2018/1/15.
 */
@Document(collection = "bd_compensatory_policy_formula")
@Data
public class CompensatoryPolicyFormula extends BaseDataEntity {

    public static final Integer ALL = 0;

    public static final Integer SPECIAL = 1;

    private String formulaTemplate;

    private Map templateMap;

}
