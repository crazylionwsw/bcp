package com.fuze.bcp.auth.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统角色定义
 * Created by sean on 16/10/10.
 */
@Document(collection = "au_sysrole")
@Data
public class SysRole extends BaseDataEntity {


    /**
     * 系统资源的ID信息
     */
    private List<String> sysResourceIds = new ArrayList<String>();

}
