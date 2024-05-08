package com.fuze.bcp.customer.domain;

import com.fuze.bcp.api.customer.bean.BusinessDataBean;
import com.fuze.bcp.api.customer.bean.CorporateActionBean;
import com.fuze.bcp.api.customer.bean.PersonalActionBean;
import com.fuze.bcp.api.customer.bean.PersonalInvestmentBean;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lily on 2017/8/3.
 */
@Document(collection = "so_customer_check")
@Data
public class CustomerCheck extends MongoBaseEntity {
    /**
     * 客户id
     */
    private String customerId;
    /**
     * 个人对外投资
     */
    private PersonalInvestmentBean personalInvestmentBean;
    /**
     * 个人诉讼
     */
    private PersonalActionBean personalActionBean;
    /**
     * 企业诉讼
     */
    private CorporateActionBean corporateActionBean;
    /**
     * 工商企业查询
     */
    private BusinessDataBean businessDataBean;

    /**
     * 扩展数据
     */
    private Map extData = new HashMap();

}
