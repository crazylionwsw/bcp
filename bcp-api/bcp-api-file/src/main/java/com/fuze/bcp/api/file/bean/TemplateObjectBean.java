package com.fuze.bcp.api.file.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.List;

/**
 * 模板原子对象 包括模板内元数据和模板code，name
 * Created by CJ on 2017/8/16.
 */
@Data
@MongoEntity(entityName="bd_templateobj")
public class TemplateObjectBean extends APIBaseDataBean {

    /**
     * 模板中元数据
     */
    private List<String> metaDatas;

    //  模板中需要的档案资料
    private List<String> imageTypeCodes;

    /**
     * 模板文件
     */
    private FileBean fileBean;

}
