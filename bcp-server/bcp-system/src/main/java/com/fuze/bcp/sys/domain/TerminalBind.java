package com.fuze.bcp.sys.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 终端绑定信息
 */
@Document(collection = "sys_terminalbind")
@Data
public class TerminalBind extends MongoBaseEntity {

    /**
     * 登录用户
     */
    private String loginUserId =  null;

    /**
     * 绑定时间
     */
    private String bindtime = null;

    /**
     * 绑定的设备
     */
    private String  terminalDeviceId;

    public String getLoginUserId() {
        return loginUserId;
    }

    public String getBindtime() {
        return bindtime;
    }

    public String getTerminalDeviceId() {
        return terminalDeviceId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public void setBindtime(String bindtime) {
        this.bindtime = bindtime;
    }

    public void setTerminalDeviceId(String terminalDeviceId) {
        this.terminalDeviceId = terminalDeviceId;
    }
}
