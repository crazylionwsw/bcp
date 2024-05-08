package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by xiaojin on 2017/3/16.
 */
@Document(collection = "sys_carsynclog")
public class CarSyncLog extends MongoBaseEntity {

    /**
     * 成功更新条数
     */
    private Integer successCount;

    /**
     * 失败条数
     */
    private Integer errorCount;

    /**
     * 同步开始时间
     */
    private String startTime;

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
