package com.fuze.bcp.file.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 模板对象 包括模板内元数据和模板code，name
 * Created by CJ on 2017/8/16.
 */
@Document(collection = "bd_templateobj")
@Data
public class TemplateObject extends BaseDataEntity {

    /**
     * 模板中元数据
     */
    private List<String> metaDatas;

    //  模板中需要的档案资料
    private List<String> imageTypeCodes;

    /**
     *      上传模板文件ID
     */
    private String fileId;

}
