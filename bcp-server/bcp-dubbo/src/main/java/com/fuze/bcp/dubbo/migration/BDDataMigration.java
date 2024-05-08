package com.fuze.bcp.dubbo.migration;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by CJ on 2017/10/16.
 */
public interface BDDataMigration {

    String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target);

}
