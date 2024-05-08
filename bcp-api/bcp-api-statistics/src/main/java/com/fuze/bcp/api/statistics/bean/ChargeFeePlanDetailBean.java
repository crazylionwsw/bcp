package com.fuze.bcp.api.statistics.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.bean.DataStatus;
import lombok.Data;

/**
 * Created by GQR on 2017/10/31.
 */
@MongoEntity(entityName = "so_chargefeeplan_detail")
@Data
public class ChargeFeePlanDetailBean extends APIBaseDataBean {
    /**
     * 收款计划ID
     */
    private String chargeFeePlanId = null;


    /**
     * 年月
     */
    private Integer index = 1;

    /**
     * 年月
     */
    private String year = null;
    private String month = null;


    /**
     * 手续费月还款
     */
    private Double chargeAmount = 0.0;

    public ChargeFeePlanDetailBean(String chargeFeePlanId) {
        this.chargeFeePlanId = chargeFeePlanId;
        setDataStatus(DataStatus.SAVE);
    }

    public ChargeFeePlanDetailBean(){

    }

}
