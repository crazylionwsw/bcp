package com.fuze.bcp.api.cardealer.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.Map;

/**
 * 渠道返佣比例对象
 */
@Data
public class DealerSharingRatioBean extends APIBaseBean {

    private String carDealerId;

    private Map<Integer, Double> sharingRatio = null;

}
