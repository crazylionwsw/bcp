package com.fuze.bcp.api.creditcar.bean.fileexpress;

import com.fuze.bcp.api.creditcar.bean.APICarBillBean;

import java.util.List;

/**
 * Created by Lily on 2017/9/15.
 */
public class FileExpressBean extends APICarBillBean {
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
    private List<String> sendCustomerImageTypeCodes;

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
