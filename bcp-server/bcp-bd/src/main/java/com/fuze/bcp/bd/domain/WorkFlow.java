package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批流的定义
 * Created by sean on 2016/10/20.
 */
@Document(collection="bd_workflow")
public class WorkFlow extends BaseDataEntity {

    /**
     * bpmn文件的ID
     */
    private String fileId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
