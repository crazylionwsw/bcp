package com.fuze.bcp.auth.domain;

import com.fuze.bcp.domain.TreeDataEntity;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 系统资源定义
 * Created by sean on 16/10/10.
 */
@Document(collection = "au_sysresource")
@Data
public class SysResource extends TreeDataEntity {

    /**
     * 操作编码
     */
    private String operationCode = null;

    /**
     * 操作名称
     */
    private String operationName = null;

    /**
     * 资源地址，从后台获取数据的地址
     */
    private String dataurl = null;

    /**
     * UI操作权限路径，针对AngularJS的UI Route的路径设置
     */
    private String url = null;

    /**
     * 是否虚拟节点
     */
    private Boolean isVirtual = false;

}
