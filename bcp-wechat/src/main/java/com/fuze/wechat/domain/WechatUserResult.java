package com.fuze.wechat.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2018/2/5.
 */
@Data
public class WechatUserResult implements Serializable {

    private Integer errcode; //	返回码
    private String errmsg;	//  对返回码的文本描述内容
    private String userid;//	成员UserID。对应管理端的帐号
    private String name;//	成员名称
    private String mobile;	//手机号码，第三方仅通讯录套件可获取
    private List department;	//成员所属部门id列表
    private List order;	//部门内的排序值，默认为0。数量必须和department一致，数值越大排序越前面。值范围是[0, 2^32)
    private String position;	//职位信息
    private String gender;	//性别。0表示未定义，1表示男性，2表示女性
    private String email;	//邮箱，第三方仅通讯录套件可获取
    private Integer isleader;	//上级字段，标识是否为上级。
    private String avatar;	//头像url。注：如果要获取小图将url最后的”/0”改成”/100”即可
    private String telephone;	//座机。第三方仅通讯录套件可获取
    private String english_name;	//英文名
    private Map extattr; //	扩展属性，第三方仅通讯录套件可获取
    private Integer status;	//激活状态: 1=已激活，2=已禁用，4=未激活。 已激活代表已激活企业微信或已关注微信插件。未激活代表既未激活企业微信又未关注微信插件。
    private Integer enable;
    private Integer hide_mobile;

}
