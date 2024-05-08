package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.MongoBaseEntity;

/**
 * 操作的历史记录
 */
public class HistoryRecord extends MongoBaseEntity {

    /**
     * 操作用户
     */
    private String loginUserId;


    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }
}
