package com.fuze.bcp.dubbo.migration;

import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.Map;

/**
 * Created by CJ on 2017/10/16.
 */
public interface DataMigration {

    public static final String SAVED = "saved";

    public static final String YES = "yes";

    public static final String NO = "no";

    Map<String, String> billDataMigration(CustomerDemand customerDemand, CustomerTransaction transaction, BasicDBObject sourceObj, BaseBillEntity targetObj, MongoTemplate source, MongoTemplate target) throws IOException;

}
