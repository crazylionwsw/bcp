package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

/**
 * 车辆品牌信息
 */
@Data
@MongoEntity(entityName = "bd_carbrand")
public class CarBrandBean extends APIBaseDataBean {

    /**
     * 外部ID
     */
    private String refMakeId;

    /**
     * 外部组(按26个字母分组)
     */
    private String groupName;

    /**
     * 品牌logo fileid
     */
    private String logoFileId = null;

}
