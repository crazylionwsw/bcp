package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;


/**
 * 车系
 */
@Data
@MongoEntity(entityName = "bd_carmodel")
public class CarModelBean extends APIBaseDataBean {

    /**
     * 品牌或厂商ID
     */
    private String carBrandId = null;

    /**
     * 外部name(制造商)
     */
    private String groupName = null;

    /**
     * 外部ID
     */
    private String refModelId = null;

    /**
     * 全称（品牌名+车系名）
     */
    private String fullName = null;

}
