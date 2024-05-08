package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 业务事件类型，定义单据中具体业务事件
 * Created by CJ on 2017/8/10.
 */
@Document(collection="bd_businesseventtype")
@Data
public class BusinessEventType extends BaseDataEntity {

    private String eventTypeCode;

}
