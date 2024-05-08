package com.fuze.bcp.domain;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.domain.BaseEntity;
import org.springframework.data.annotation.Id;


/**
 * MongoDB数据库基类
 */
public class  MongoBaseEntity extends BaseEntity implements DataStatus {

    private static final long serialVersionUID = -1L;
    /**
     * 主键信息
     */
    @Id
    private String id;

    /**
     * 备注信息
     */
    private String comment;

    /**
     * 数据状态：暂存，保存，作废， 锁定状态
     */
    private Integer dataStatus = SAVE;


    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public MongoBaseEntity(String id) {
        this.id = id;
    }

    public MongoBaseEntity() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
