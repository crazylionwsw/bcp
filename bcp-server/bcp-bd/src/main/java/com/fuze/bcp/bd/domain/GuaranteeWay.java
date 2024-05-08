package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 担保方式定义
 * Created by sean on 2016/10/20.
 */
@Document(collection="bd_guaranteeway")
public class GuaranteeWay extends BaseDataEntity {

    /**
     * 是否需要担保函
     */
    private Boolean requiredGuaranteeLetter = true;

    /**
     * 是否需要抵押资料
     */
    private Boolean requiredPledgeFile = true;

    /**
     * 抵押资料列表
     */
    @Transient
    private List<CustomerImageType> customerImageTypeList = new ArrayList<CustomerImageType>();
    private List<String>  customerImageTypeIds;


    public Boolean getRequiredGuaranteeLetter() {
        return requiredGuaranteeLetter;
    }

    public void setRequiredGuaranteeLetter(Boolean requiredGuaranteeLetter) {
        this.requiredGuaranteeLetter = requiredGuaranteeLetter;
    }

    public Boolean getRequiredPledgeFile() {
        return requiredPledgeFile;
    }

    public void setRequiredPledgeFile(Boolean requiredPledgeFile) {
        this.requiredPledgeFile = requiredPledgeFile;
    }

    public List<CustomerImageType> getCustomerImageTypeList() {
        return customerImageTypeList;
    }

    public void setCustomerImageTypeList(List<CustomerImageType> customerImageTypeList) {
        this.customerImageTypeList = customerImageTypeList;
    }

    public List<String> getCustomerImageTypeIds() {
        return customerImageTypeIds;
    }

    public void setCustomerImageTypeIds(List<String> customerImageTypeIds) {
        this.customerImageTypeIds = customerImageTypeIds;
    }
}
