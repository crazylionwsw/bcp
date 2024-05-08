package com.fuze.bcp.file.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ on 2017/9/12.
 */
@Data
@Document(collection = "bd_pushobject")
public class PushObject extends BaseDataEntity {

    /**
     * 邮件内容模板
     */
    private String contentTemplateId;

    /**
     * 标题
     */
    private String subject;

}
