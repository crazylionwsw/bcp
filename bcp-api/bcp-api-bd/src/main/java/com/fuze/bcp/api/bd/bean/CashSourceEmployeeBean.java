package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import lombok.Data;

/**
 * 资金提供方-员工信息
 * Created by sean on 16/10/10.
 */
@Data
@MongoEntity(entityName = "bd_cashsourceemployee")
public class CashSourceEmployeeBean extends EmployeeBean {

    /**
     * 微信openid
     */

    /**
     * 所属组织机构
     */
    private String cashSourceId;

}
