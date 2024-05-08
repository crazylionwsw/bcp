package com.fuze.bcp.api.file.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ on 2017/9/12.
 */
@Data
@MongoEntity(entityName = "bd_emailobject")
public class EmailObjectBean extends APIBaseDataBean {

    /**
     * 附件documentBean;
     */
    private List<String> documentIds = new ArrayList();

    /**
     * 邮件内容模板
     */
    private String contentTemplateId;

    /**
     * 标题
     */
    private String subject;

}
