package com.fuze.bcp.api.msg.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * Created by CJ on 2017/9/14.
 */
@Data
@MongoEntity(entityName = "msg_source")
public class SubSribeSourceBean extends APIBaseBean {

    public static final String BD_CASHSOURCEEMPLOYEE = "bd_cashsourceemployee";

    public static final String BD_EMPLOYEE = "bd_employee";

    public static final String BD_DEALEREMPLOYEE = "bd_dealeremployee";

    public static final String SO_CUSTOMER = "so_customer";

    private String name;

    private String groupName;

    private String phoneNo;

    private String email;

    private String vcharNo;

    private String userType;

    private String loginUserName;

}
