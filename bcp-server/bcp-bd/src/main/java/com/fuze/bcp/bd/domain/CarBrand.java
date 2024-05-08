package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 车辆品牌信息
 */
@Document(collection="bd_carbrand")
@Data
public class CarBrand extends BaseDataEntity {

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

    public String getRefMakeId() {
        return refMakeId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getLogoFileId() {
        return logoFileId;
    }

    public void setRefMakeId(String refMakeId) {
        this.refMakeId = refMakeId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setLogoFileId(String logoFileId) {
        this.logoFileId = logoFileId;
    }
}
