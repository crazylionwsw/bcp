package com.fuze.bcp.api.cardealer.bean;

import com.fuze.bcp.bean.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 4S 店销售政策
 */
@Data
public class CarDealerRateBean implements Serializable {

    /**
     * 终端销售手续费率
     */
    private List<RateType> bankRates = new ArrayList<RateType>();

    /**
     * 贷款服务费率
     */
    private List<RateType> serviceRates = new ArrayList<RateType>();

}