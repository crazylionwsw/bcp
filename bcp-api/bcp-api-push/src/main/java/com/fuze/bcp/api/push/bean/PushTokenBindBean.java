package com.fuze.bcp.api.push.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * Created by CJ on 2017/10/11.
 */
@Data
public class PushTokenBindBean extends APIBaseBean {

    private String loginUserName;

    private String userToken;

}
