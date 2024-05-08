package com.fuze.bcp.sys.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by admin on 2017/7/7.
 */
@Data
@Document(collection = "sys_action")
public class ActionItem extends BaseDataEntity{
    //操作编辑、名称、描述
}
