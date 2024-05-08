package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户的档案资料
 * Created by Gumenghao on 2016/10/19.
 */
@Document(collection = "cus_imagefile")
@Data
public class CustomerImageFile extends MongoBaseEntity {

    /**
     * 客户
     */
    private String customerId;

    /**
     * 客户交易ID
     */
    private String customerTransactionId;

    /**
     * 资料类型
     */
    private String customerImageTypeCode = null;

    /**
     * 文件ID列表
     */
    private List<String> fileIds = new ArrayList<String>();

    /**
     * 上传人
     */
    private String loginUserId;

    /**
     * 图片解析是否成功，解析失败为false，解析成功为true
     */
    private Boolean resolvedStatus = false;

    /**
     * 是否有效
     */
    private Boolean isValid = true;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerTransactionId() {
        return customerTransactionId;
    }

    public void setCustomerTransactionId(String customerTransactionId) {
        this.customerTransactionId = customerTransactionId;
    }

    public String getCustomerImageTypeCode() {
        return customerImageTypeCode;
    }

    public void setCustomerImageTypeCode(String customerImageTypeCode) {
        this.customerImageTypeCode = customerImageTypeCode;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public Boolean getResolvedStatus() {
        return resolvedStatus;
    }

    public void setResolvedStatus(Boolean resolvedStatus) {
        this.resolvedStatus = resolvedStatus;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
