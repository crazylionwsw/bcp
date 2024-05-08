package com.fuze.bcp.api.push.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by CJ on 2017/9/28.
 */
@Data
public class PushDataBean extends APIBaseBean implements Serializable {
    public static final Integer go_app = 1;
    public static final Integer go_url = 2;
    public static final Integer go_activity = 3;
    public static final Integer go_custom = 4;

    private String ticker;

    private String title;

    private String text;

    private Integer afterOpenAction;

    private String url;

    private String activity;

    private String customer;

    /**
     * 自定义参数
     */
    private Map<String, String> extraFields;

}
