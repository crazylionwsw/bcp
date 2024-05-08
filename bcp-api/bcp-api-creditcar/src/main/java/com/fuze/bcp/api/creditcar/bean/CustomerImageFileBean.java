package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户的档案资料
 * Created by Gumenghao on 2016/10/19.
 */
@Data
public class CustomerImageFileBean extends APIBaseBean {

    /**
     * 客户
     */
    private String customerId;

    /**
     * 客户交易ID
     */
    private String customerTransactionId;

    /**
     * 档案类型Id
     */
    private String customerImageTypeCode = null;

    /**
     * 档案类型
     */
    private CustomerImageTypeBean customerImageType = null;

    /**
     * 文件ID列表
     */
    private List<String> fileIds = new ArrayList<String>();

    /**
     * 上传人
     */
    private String loginUserId;

    /**
     * 封面文件id
     */
    private String coverFileId = null;

    /**
     * 图片解析是否成功，解析失败为false，解析成功为true
     */
    private Boolean resolvedStatus = false;

    /**
     * 是否有效
     */
    private Boolean isValid = true;


    public String getCoverFileId() {
        if (this.fileIds.size() > 0) {
            return this.fileIds.get(0);
        } else {
            return null;
        }
    }
}
