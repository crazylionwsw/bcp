package com.fuze.bcp.api.credithome.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 家装分期
 */
@Data
public class DomesticOutfitSubmissionBean extends HomeBillSubmissionBean {

    private String customerName = "";

    private Integer gender = 0;

    private String identifyNo;

    private String cell;

    private String companyName = null;

    private List<String> houseAddress = new ArrayList<String>();

    private String houseDetailAddress = "";

    private String customerComment = "";

    private List<String> cashSourceIds = new ArrayList<String>();

    private List<String> channelIds = new ArrayList<String>();

    private List<String> employeeIds = new ArrayList<String>();

    /**
     *  申请额度
     */
    private Double applyAmount = null;

    /**
     *  申请日期
     */
    private String applyTime  = null;

    /**
     *  批贷金额
     */
    private Double approvedAmount  = null;

    /**
     *  批贷日期
     */
    private String approvedTime  = null;

    /**
     *  放款金额
     */
    private Double loanAmount  = null;

    /**
     *  放款日期
     */
    private String loanTime  = null;

}
