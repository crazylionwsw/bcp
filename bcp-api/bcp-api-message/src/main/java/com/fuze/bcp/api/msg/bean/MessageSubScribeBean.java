package com.fuze.bcp.api.msg.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 2017/5/24.
 * 消息订阅对象
 */
@Data
public class MessageSubScribeBean extends APIBaseBean {

    public static final String BD_CASHSOURCEEMPLOYEE = "bd_cashsourceemployee";

    public static final String BD_EMPLOYEE = "bd_employee";

    public static final String BD_DEALEREMPLOYEE = "bd_dealeremployee";

    public static final String SO_CUSTOMER = "so_customer";

    /**
     * 业务类型
     */
    private String businessType = null;

    /**
     * 订阅人ID
     */
    private List<String> subScribeSourceIds;

    /**
     * 订阅类型
     */
    private String scribeType;


    /**
     * 发送渠道
     */
    private List<String> userChannel = new ArrayList<>();

}
