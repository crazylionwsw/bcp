package com.fuze.bcp.api.msg.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by sean on 2017/5/23.
 * 消息发布日志
 */
@Data
public class MessageLogBean extends APIBaseBean {

    public static final Integer SUCCESS = 1;

    public static final Integer FAILD = 0;

    /**
     * 接收人
     */
    private String to;

    /**
     * 生成后的消息发布内容
     */
    private Map content;

    private Integer result;

}
