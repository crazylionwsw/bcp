package com.fuze.bcp.api.wechat.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by CJ on 2018/4/23.
 */
@Data
public class WechatUserResult2 implements Serializable {

    private String openid;
    private String nickname;
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String privilege;
    private String unionid;
    private String errcode;
    private String errmsg;


}
