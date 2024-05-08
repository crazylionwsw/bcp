package com.fuze.bcp.bd.domain;

import com.fuze.bcp.api.bd.bean.RoleControl;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**客户资料类型
 *
 * Created by sean on 2016/10/20.
 */
@Document(collection = "bd_customerimagetype")
@Data
public class CustomerImageType extends BaseDataEntity {

    /**
     * 分组标签
     */
    private String groupLabel = null;

    /**
     * 是否进行图片解析
     */
    private Boolean resolved = false;

    /**
     * 是否启用该档案类型，false表示不启用，业务不能使用该档案类型。true表示业务可以使用档案类型
     */
    private Boolean valid = true;

    /**
     * 是否需要合成
     */
    private Boolean merged = false;

    /**
     * 合成模板
     */
    private String mergeTemplateId;

    /**
     * 是否需要生成
     */
    private Boolean generated = false;

    /**
     * 生成模板
     */
    private String generateTemplateId;

    /**
     * 是否可以追加档案文件
     */
    private Boolean canAppend = false;

    /**
     * 档案支持的文件后缀名
     */
    private List<String>    suffixes = new ArrayList<String>();

    /**
     * 样本的文件ID
     */
    private List<String>    exampleFileIds = new ArrayList<String>();

    /**
     * 角色权限
     */
    private List<RoleControl> roleControls = new ArrayList<RoleControl>();

}
