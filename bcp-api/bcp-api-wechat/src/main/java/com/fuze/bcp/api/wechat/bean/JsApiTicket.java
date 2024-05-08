package com.fuze.bcp.api.wechat.bean;

import lombok.Data;

import java.io.Serializable;

/**
 *  jsapi_ticket
 */
@Data
public class JsApiTicket implements Serializable{

    // 错误code
    private String errcode;

    // 错误msg
    private String errmsg;

    // 获取到的凭证
    private String jsapi_ticket;

    // 凭证有效时间，单位：秒
    private int expires_in;

    @Override
    public String toString() {
        return "JsApiTicket{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", jsapi_ticket='" + jsapi_ticket + '\'' +
                ", expires_in=" + expires_in +
                '}';
    }

}
