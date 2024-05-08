package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 产品分类
 */
@Document(collection = "bd_productcatalog")
public class ProductCatalog extends BaseDataEntity {

    /**
     * 父级分类
     */
    @Transient
    private ProductCatalog parentCatalog = null;

    /**
     * 父类ID
     */
    private String parentId = null;


    public ProductCatalog getParentCatalog() {
        return parentCatalog;
    }

    public void setParentCatalog(ProductCatalog parentCatalog) {
        this.parentCatalog = parentCatalog;
    }
}
