package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.BusinessRate;
import com.fuze.bcp.bean.RateType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业务类型
 * Created by sean on 2016/10/20.
 */
@Data
public class BusinessTypeRateLookupBean extends APILookupBean {

    private List<BusinessRate> rateTypes;

    private List<SourceRateBean> sourceRates;

}
