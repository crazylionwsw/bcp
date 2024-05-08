package com.fuze.bcp.sys.domain;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.domain.BaseDataEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by admin on 2017/3/14.
 */
@Document(collection="sys_parameter")
public class SysParam extends BaseDataEntity {

    public SysParam() {
        super.setDataStatus(DataStatus.SAVE);
    }

    /**
     * 是否允许下级继承
     */
    Boolean  canExtends = true;

    /**
     * 参数值 (JSON字符串)
     */
    String  parameterValue = null;


    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValues) {
        this.parameterValue = parameterValues;
    }

    public Boolean getCanExtends() {
        return canExtends;
    }

    public void setCanExtends(Boolean canExtends) {
        this.canExtends = canExtends;
    }
}
