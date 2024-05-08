package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 车系
 */
@Document(collection="bd_carmodel")
@Data
public class CarModel extends BaseDataEntity {

    /**
     * 品牌或厂商ID
     */
    private String carBrandId;

    /**
     * 外部name(制造商)
     */
    private String groupName;

    /**
     * 外部ID
     */
    private String refModelId;

    /**
     * 全称（品牌名+车系名）
     */
    private String fullName = null;

}
