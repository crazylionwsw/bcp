package com.fuze.bcp.api.statistics.bean;

import java.io.Serializable;

/**
 * Created by GQR on 2017/9/23.
 */

public class QueryFilter implements Serializable{
    private String customerName;
    private String customerCell;
    private String customerIdentifyNo;
    private String businessTime;
    private Integer status = -1 ;
    private Integer approveStatus = -1 ;
    private String approveDate;
    private String startTime;
    private String endTime;
    private Boolean advancedPay = false;

    public String getEndTime() {
        return endTime;
    }
    public Integer getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Integer approveStatus) {
        this.approveStatus = approveStatus;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }
    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }


    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCell() {
        return customerCell;
    }

    public void setCustomerCell(String customerCell) {
        this.customerCell = customerCell;
    }

    public String getCustomerIdentifyNo() {
        return customerIdentifyNo;
    }

    public void setCustomerIdentifyNo(String customerIdentifyNo) {
        this.customerIdentifyNo = customerIdentifyNo;
    }

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    public Boolean getAdvancedPay() {
        return advancedPay;
    }

    public void setAdvancedPay(Boolean advancedPay) {
        this.advancedPay = advancedPay;
    }
}
