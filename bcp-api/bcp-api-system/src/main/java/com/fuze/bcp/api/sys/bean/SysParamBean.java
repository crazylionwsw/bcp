package com.fuze.bcp.api.sys.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

/**
 * Created by admin on 2017/3/14.
 */
@Data
@MongoEntity(entityName = "sys_parameter")
public class SysParamBean extends APIBaseDataBean {

    /**
     * 是否允许下级继承
     */
    Boolean  canExtends = true;

    /**
     * 参数值 (JSON字符串)
     */
    String  parameterValue = null;

}
