package com.fuze.bcp.api.workflow.bean;

import com.fuze.bcp.utils.DateTimeUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/17.
 */
@Data
public class SignInfo implements Serializable {
    /**
     * 签批初始状态
     */
    public final static int SIGN_INIT = 0;

    /**
     * 签批通过
     */
    public final static int SIGN_PASS = 2;

    /**
     * 签批被驳回
     */
    public final static int SIGN_REAPPLY = 8;

    /**
     * 签批被拒绝
     */
    public final static int SIGN_REJECT = 9;

    /**
     * 标志--备注
     */
    public final static int FLAG_COMMENT = 0;

    /**
     * 标志--提交
     */
    public final static int FLAG_COMMIT = 1;

    /**
     * 标志--审查、审核
     */
    public final static int FLAG_SIGN = 2;

    /**
     * 标志--重审
     */
    public final static int FLAG_AGAIN_COMMIT = 3;

    /**
     * 签批用户
     */
    private String userId;

    /**
     * 签批人
     */
    private String employeeId;

    /**
     * 签批状态
     */
    private Integer result = SIGN_INIT;

    /**
     *  标志状态
     */
    private Integer flag = FLAG_COMMENT;

    /**
     * 该签批信息是否来自业务员
     */
    private Boolean fromSalesman;

    /**
     *  操作任务信息
     */
    private String taskInfo = null;

    /**
     * 时间戳
     */
    private String ts = null;

    /**
     * 说明
     */
    private String comment = null;

    /**
     * TODO 考虑增加到业务操作日志
     */
    private List<String> noteTemplateIds = new ArrayList<String>();

    public SignInfo(String userId, String employeeId, Integer approveStatus, String comment){
        this.userId = userId;
        this.employeeId = employeeId;
        this.result = approveStatus;
        this.comment = comment;
        this.fromSalesman = true;
        updateTs();
    }

    public SignInfo(String userId, String employeeId, Integer approveStatus,Integer flag, String comment){
        this.userId = userId;
        this.employeeId = employeeId;
        this.result = approveStatus;
        this.flag = flag;
        this.comment = comment;
        this.fromSalesman = true;
        updateTs();
    }

    public SignInfo() {
        updateTs();
    }


    public void updateTs(){
        setTs(DateTimeUtils.getCreateTime());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Boolean getFromSalesman() {
        return fromSalesman;
    }

    public void setFromSalesman(Boolean fromSalesman) {
        this.fromSalesman = fromSalesman;
    }

    public List<String> getNoteTemplateIds() {
        return noteTemplateIds;
    }

    public void setNoteTemplateIds(List<String> noteTemplateIds) {
        this.noteTemplateIds = noteTemplateIds;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
