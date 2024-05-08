package com.fuze.bcp.api.customer.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.List;

/**
 * 客户资料打包
 * Created by sean on 2016/11/27.
 */
@Data
public class CustomerPackageBean extends APIBaseBean {

    /**
     * 客户
     */
    private String customerId;

    /**
     * 客户交易ID
     */
    private String customerTransactionId;

    /**
     * 资金机构
     */
    private String cashSourceId;

    /**
     * 压缩文件路径
     */
    private String urlPath;

    /**
     * 打包人
     */
    private String loginUserId;


}
