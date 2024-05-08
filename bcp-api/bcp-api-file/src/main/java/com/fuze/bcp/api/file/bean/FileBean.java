package com.fuze.bcp.api.file.bean;

import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.utils.NumberHelper;
import com.fuze.bcp.utils.QingStorUtils;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Created by admin on 2017/6/15.
 */
@Data
public class FileBean extends APIBaseBean {

    public FileBean() {
    }

    public FileBean(Long contentLength, String fileName, String bucketName, String zoneName, String objectname, String contentType) {
        this.contentLength = contentLength;
        this.fileName = fileName;
        this.bucketName = bucketName;
        this.zoneName = zoneName;
        this.objectname = objectname;
        this.contentType = contentType;
    }

    public FileBean(MultipartFile mfile) {
        this.setFileName(mfile.getOriginalFilename());
        this.setContentType(mfile.getContentType());
        this.setContentLength(mfile.getSize());
        this.setBucketName(QingStorUtils.DEFAULT_BUCKET);
        this.setZoneName(QingStorUtils.DEFAULT_ZOO);
        this.setObjectname(NumberHelper.getRundomCode());
    }

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

    private  String url;

    private String contentType;
}
