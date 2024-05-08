package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 文件快递单信息
 * Created by sean on 2016/11/27.
 */
@Document(collection = "so_fileexpress")
@Data
public class FileExpress extends BaseBillEntity {

    /**
     * 发件人
     */
    private String sendManId;

    /**
     * 发送时间
     */
    private String sendTime;

    /**
     * 发件地址
     */
    private String sendAddress;

    /**
     * 快递单号
     */
    private String code;

    /**
     * 快递的资料信息
     */
    private List<String>   sendCustomerImageTypeCodes;

    /**
     * 接收人
     */
    private String   receiveManId = null;

    /**
     * 接收时间 年月日 时 分，需要精确到分钟
     */
    private String receiveTime = null;

    /**
     * 接收地址
     */
    private String receiveAddress = null;

    /**
     * 快递公司
     */
    private String expressFirm = null;

    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A006";
    }
}
