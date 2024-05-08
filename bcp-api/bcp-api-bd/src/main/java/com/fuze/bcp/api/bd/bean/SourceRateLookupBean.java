package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.PayAccount;
import com.fuze.bcp.bean.RateType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 资金利率
 */
@Data
public class SourceRateLookupBean extends APILookupBean {

    /**
     * 资金来源信息
     */
    private String cashSourceId = null;

    /**
     * 期数及对应的利率表
     */
    private List<RateType> rateTypes;


}
