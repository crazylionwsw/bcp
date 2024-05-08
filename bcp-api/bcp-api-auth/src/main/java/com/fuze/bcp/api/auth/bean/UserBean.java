package com.fuze.bcp.api.auth.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录用户
 * Created by sean on 2017/5/19.
 */
@Data
public class UserBean implements Serializable {

    /**
     * 令牌
     */
    private String token;

    /**
     * 用户ID
     */
    private String userID;

    /**
     * 用户姓名
     */
    private String displayName;

    /**
     * 用户头像
     */
    private String avatarFileId;

    /**
     * 登录时间
     */
    private String lastLoginTime = null;

    /**
     * 角色编码列表
     */
    private List<String> roles;


}
