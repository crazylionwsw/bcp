package com.fuze.bcp.api.auth.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APITreeDataBean;
import lombok.Data;

/**
 * 系统资源定义
 * Created by sean on 16/10/10.
 */
@Data
@MongoEntity(entityName = "au_sysresource")
public class SysResourceBean extends APITreeDataBean {

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
