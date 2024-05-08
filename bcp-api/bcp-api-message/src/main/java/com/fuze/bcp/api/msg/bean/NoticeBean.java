package com.fuze.bcp.api.msg.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.Set;

/**
 * Created by CJ on 2017/10/30.
 */
@Data
public class NoticeBean extends APIBaseBean implements Channels {

    public static final String TYPE_1 = "type_1"; // 通知
    public static final String TYPE_2 = "type_2"; // 公告
    public static final String TYPE_3 = "type_3"; // 消息


    // 消息类型 通知、公告、消息
    private String type;
    // 发送类型
    private String sendType;
    // 标题
    private String title;
    // 内容
    private String content;

    // 1 人，2角色，3组织架构
    private Integer fromGroup;

    private Set<String> loginUserNames;

    private String groupId;

    private String orgId;

    // 发送状态 1 未发送、2发送中、3发送成功、4发送失败
    private Integer status = 1;
    /**
     * 发送结果
     *  1 表示成功
     *  0 表示失败
     */
    private Integer result;

    /*********************************pad回显消息时使用********************************/

    private String goActivity;

    private Integer afterOpenAction;

    private String billId;

    private String businessType;

    private String sendTime;

    private String orderId;

}
