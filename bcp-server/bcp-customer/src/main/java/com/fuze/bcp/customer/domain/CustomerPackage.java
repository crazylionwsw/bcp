package com.fuze.bcp.customer.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 客户资料打包
 * Created by sean on 2016/11/27.
 */
@Document(collection = "cus_package")
@Data
public class CustomerPackage extends MongoBaseEntity {

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

    @Transient
    private List<String> imageFileIds;

}
