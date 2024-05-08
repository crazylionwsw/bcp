package com.fuze.wechat.domain;

import com.fuze.wechat.base.DataStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by ZQW on 2018/5/17.
 */
@Document(collection = "so_public_user")
@Data
public class PublicUser implements Serializable, DataStatus{

    private static final long serialVersionUID = -1L;
    /**
     * 主键信息
     */
    @Id
    private String id;

    /**
     * 备注信息
     */
    private String comment;

    /**
     * 数据状态：暂存，保存，作废， 锁定状态
     */
    private Integer dataStatus = SAVE;

    private String openid;

    private String nickname;

    private Integer subscribe;

    private String headimgurl;

    private String cell;

    private String ts;

    /**
     *      创建方式
     *      WXL    以微信登录的方式创建用户
     *      CL     以手机号登录的方式创建用户
      */
    private String createType = "CL";

}
