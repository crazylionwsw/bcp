package com.fuze.bcp.sys.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 系统登录日志
 */
@Document(collection = "sys_loginlog")
public class LoginLog extends MongoBaseEntity {

    /**
     * 登录成功
     */
    public final static int STATUS_SUCCEED = 0;

    /**
     * 登录失败
     */
    public final static int STATUS_FAILED = 9;

    /**
     * 绑定的设备信息
     */
    @DBRef
    private TerminalBind terminalBind = null;

    /**
     * 登录的时间
     */
    private String logintime = null;

    /**
     * 登录状态
     */
    private Integer status = 0;
    /**
     * 系统账号
     */
    private String loginUserId = null;


    public TerminalBind getTerminalBind() {

        return terminalBind;
    }

    public void setTerminalBind(TerminalBind terminalBind) {
        this.terminalBind = terminalBind;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
