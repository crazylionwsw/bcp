package com.fuze.bcp.file.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.InputStream;

/**
 * Created by CJ on 2017/6/20.
 */
@Document(collection="file_bcpfile")
@Data
public class BcpFile extends MongoBaseEntity {

    /**
     * 文件长度（尺寸）
     */
    private Long contentLength = 0L;

    /**
     * 文件名
     */
    private String fileName = null;

    /**
     * 青云属性，对象所在bucketName
     */
    private String bucketName = null;

    /**
     * 青云属性，对象所在zoneName
     */
    private String zoneName = null;

    /**
     * 青云属性，对象名称
     */
    private String objectname = null;

    private String contentType;

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getObjectname() {
        return objectname;
    }

    public void setObjectname(String objectname) {
        this.objectname = objectname;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
