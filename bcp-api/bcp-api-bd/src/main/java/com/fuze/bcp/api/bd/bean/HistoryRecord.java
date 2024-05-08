package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 操作的历史记录
 */
@Data
public class HistoryRecord extends APIBaseBean {

    /**
     * 操作用户
     */
    private String loginUserId;

}
