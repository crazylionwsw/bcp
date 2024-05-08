package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 客户资料归还信息
 * Created by sean on 2016/11/27.
 */
@Document(collection = "so_return_info")
@Data
public class ReturnInfo extends BaseBillEntity {

    /**
     * 取件代理人名称
     */
    private String agentName = null;

    /**
     * 取件代理人手机
     */
    private String agentCell = null;

    /**
     * 取件代理人身份证号码
     */
    private String agentIdentifyNo = null;

    /**
     * 取件验证码
     */
    private String validateCode = null;

    /**
     * 预约取件时间
     */
    private String bookTime = null;

    /**
     * 归还时间
     */
    private String returnTime = null;

    /**
     * 归还的档案资料
     */
    private List<String> customerImageTypeCodes;

    /**
     * 子类需要定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A009";
    }
}
