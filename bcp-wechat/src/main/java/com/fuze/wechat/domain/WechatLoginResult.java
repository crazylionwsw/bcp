package com.fuze.wechat.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by CJ on 2018/2/2.
 */
@Data
public class WechatLoginResult implements Serializable {

    private String UserId; //成员UserID

    private String DeviceId; //手机设备号(由企业微信在安装时随机生成，删除重装会改变，升级不受影响)

    private String errcode; //返回码

    private String errmsg; //对返回码的文本描述内容

    @Override
    public String toString() {
        return "Result{" +
                "UserId='" + UserId + '\'' +
                ", DeviceId='" + DeviceId + '\'' +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }


}
