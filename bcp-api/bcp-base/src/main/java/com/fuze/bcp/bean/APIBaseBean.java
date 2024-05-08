package com.fuze.bcp.bean;

import com.fuze.bcp.utils.DateTimeUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口数据结构的基类定义
 * Created by sean on 2017/5/19.
 */
@Data
public class APIBaseBean implements Serializable,DataStatus {


    /**
     * 对象的ID
     */
    private String id = null;

    /**
     * 时间戳
     */
    private String ts = null;

    /**
     * 说明
     */
    private String comment = null;

    /**
     * 数据状态：暂存，保存，作废， 锁定状态
     */
    private Integer dataStatus = SAVE;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }
}
