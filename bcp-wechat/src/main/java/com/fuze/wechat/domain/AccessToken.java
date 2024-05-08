package com.fuze.wechat.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by CJ on 2018/2/2.
 */
@Data
public class AccessToken implements Serializable{

    // 错误code
    private String errcode;

    // 错误msg
    private String errmsg;

    // 获取到的凭证
    private String access_token;

    // 凭证有效时间，单位：秒
    private int expires_in;

    @Override
    public String toString() {
        return "AccessToken{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                '}';
    }

}
