package com.fuze.bcp.api.msg.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * Created by CJ on 2017/12/8.
 */
@Data
public class NoticeLookupBean extends APIBaseBean implements Channels {

    public NoticeLookupBean() {
    }

    public NoticeLookupBean(NoticeLookupBean n) {
        this.type = n.getType();
        this.sendType = n.getSendType();
        this.title = n.getTitle();
        this.content = n.getContent();
        this.goActivity = n.getGoActivity();
        this.afterOpenAction = n.getAfterOpenAction();
        this.billId = n.getBillId();
        this.businessType = n.getBusinessType();
        this.sendTime = n.getSendTime();
    }


    // 消息类型 通知、公告、消息
    private String type;

    // 发送类型
    private String sendType;

    // 标题
    private String title;

    // 内容
    private String content;

    /******************************pad回显消息时使用*********************************/

    private String goActivity;

    private Integer afterOpenAction;

    private String billId;

    private String businessType;

    private String sendTime;

    private String orderId;

}
