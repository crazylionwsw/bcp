package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

/**
 * 收费明细项
 */
@Data
public class FeeValueBean extends APIBaseDataBean {

    /**
     * 费用
     */
    private Double fee = 0.0;


}
