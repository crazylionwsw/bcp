package com.fuze.bcp.dubbo.migration.mongo;

import lombok.Data;

/**
 * Created by CJ on 2017/10/17.
 */
@Data
public class ParamObject {

    public ParamObject(String serverName, String oldCollectionName, String newName) {
        this.serverName = serverName;
        this.oldCollectionName = oldCollectionName;
        this.newName = newName;
    }

    String serverName;

    String oldCollectionName;

    String newName;

}
