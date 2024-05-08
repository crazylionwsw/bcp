package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

/**
 * 贴息政策
 */
@Data
public class CompensatoryPolicyListBean extends APIBaseDataBean {

    /**
     * 车辆品牌
     */
    private String carBrandId;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 截止日期
     */
    private String endDate;


}
