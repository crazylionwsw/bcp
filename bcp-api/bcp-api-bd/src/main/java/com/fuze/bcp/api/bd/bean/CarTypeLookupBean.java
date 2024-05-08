package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APILookupBean;
import lombok.Data;

/**
 * 车型信息列表对象
 */
@Data
public class CarTypeLookupBean extends APILookupBean {

    /**
     * 车系名称
     */
    private String carModel;

    /**
     * 全称
     */
    private String fullName;

}
