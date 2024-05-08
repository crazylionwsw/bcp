package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

/**
 * Created by CJ on 2017/8/10.
 */
@Data
@MongoEntity(entityName = "bd_businesseventtype")
public class BusinessEventTypeBean extends APIBaseDataBean {

    private String eventTypeCode;

}
