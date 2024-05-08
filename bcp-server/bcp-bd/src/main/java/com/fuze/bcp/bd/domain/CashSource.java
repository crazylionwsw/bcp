package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.TreeDataEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 资金提供方管理，专业术语成为贷款人，可以是银行，金融贷款机构，P2P资金，个人资金，自有资金
 * Created by sean on 2016/10/20.
 */
@Data
@Document(collection="bd_cashsource")
public class CashSource extends TreeDataEntity {

    /**
     * 资金提供方：银行
     */
    public final static int TYPE_BANK = 0;


    /**
     * 金融贷款公司
     */
    public final static int TYPE_FIRM = 1;


    /**
     * P2P产品
     */
    public final static int TYPE_P2P = 2;


    /**
     * 个人资金
     */
    public final static int TYPE_PERSONAL = 3;


    /**
     * 自有资金
     */
    public final static int TYPE_SELF = 9;


    /**
     * 简称
     */
    private String shortName = null;

    /**
     *  联系人
     */
    private String contacts = null;

    /**
     *  联系电话
     */

    private String cell = null;

    /**
     * 所在地址
     */
    private String address = null;


    /**
     * 来源类型
     */
    private Integer sourceType = TYPE_BANK;

    /**
     * 支行营销代码
     */
    private List<String> marketingCode = new ArrayList<String>();


}
