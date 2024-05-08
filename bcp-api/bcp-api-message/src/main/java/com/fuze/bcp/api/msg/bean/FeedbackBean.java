package com.fuze.bcp.api.msg.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * Created by CJ on 2017/10/23.
 */
@Data
public class FeedbackBean extends APIBaseBean {

//    private Integer launch;
    //类型
    /**
     * 0 产品建议
     * 1 程序错误
     * 2 其他意见
     */
    private String type;
    //标题
    private String title;
    //内容
    private String content;
    //联系人
    private String connect;

//    private String parentId;
//
//    private String rootId;
//
//    private String loginUserId;

}
