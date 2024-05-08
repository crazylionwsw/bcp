package com.fuze.bcp.api.file.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

/**
 * Created by CJ on 2017/10/13.
 */
@Data
@MongoEntity(entityName = "bd_pushobject")
public class PushObjectBean extends APIBaseDataBean {

    /**
     * 邮件内容模板
     */
    private String contentTemplateId;

    /**
     * 标题
     */
    private String subject;

}
