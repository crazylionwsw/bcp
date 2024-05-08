package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APITreeDataBean;
import lombok.Data;

/**
 * 地区信息
 */
@Data
@MongoEntity(entityName = "bd_province")
public class ProvinceBean extends APITreeDataBean {

}
