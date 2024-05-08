package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 车辆用途
 */
@Document(collection = "bd_creditusage")
public class CreditUsage extends BaseDataEntity {

}
