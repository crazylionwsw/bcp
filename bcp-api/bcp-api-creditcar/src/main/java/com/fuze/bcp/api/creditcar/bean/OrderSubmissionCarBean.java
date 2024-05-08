package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户签约提交单（PAD）  车
 */
@Data
public class OrderSubmissionCarBean implements Serializable {


    // 车
    // 经销商信息    所需字段 来源经销商，经销商店面地址，销售人员姓名，销售人员电话
    // 车辆信息                               1
    // 车辆登记信息                       和车辆信息一起保存在customercar里，
    // 车辆评估信息                           1


    /**
     * 来源经销商id
     */
    private String carDealerId;
    /**
     * 来源经销商名称
     */
    private String carDealerMame;
    /**
     * 4S店销售人员
     */
    private String dealerEmployeeId;
    /**
     * 销售人员姓名
     */
    private String dealerEmployeeName;
    /**
     * 销售人员电话
     */
    private String dealerEmployeeCell;

    /**
     * 车辆信息
     */
    private CustomerCarBean customerCar = new CustomerCarBean();

    /**
     * 车辆评估信息
     */
    private CarValuationBean carValuation = new CarValuationBean();

    /**
     * 预约提车日期
     */
    private String pickCarDate = null;

    /**
     * 全称（品牌名+车系名+车型名）
     */
    private String CarTypeFullName = null;

    /**
     * 业务类型 (新车/二手车）
     */
    private String businessTypeCode;


    public String getCarDealerId() {
        return carDealerId;
    }

    public void setCarDealerId(String carDealerId) {
        this.carDealerId = carDealerId;
    }

    public String getCarDealerMame() {
        return carDealerMame;
    }

    public void setCarDealerMame(String carDealerMame) {
        this.carDealerMame = carDealerMame;
    }

    public String getDealerEmployeeId() {
        return dealerEmployeeId;
    }

    public void setDealerEmployeeId(String dealerEmployeeId) {
        this.dealerEmployeeId = dealerEmployeeId;
    }

    public String getDealerEmployeeName() {
        return dealerEmployeeName;
    }

    public void setDealerEmployeeName(String dealerEmployeeName) {
        this.dealerEmployeeName = dealerEmployeeName;
    }

    public String getDealerEmployeeCell() {
        return dealerEmployeeCell;
    }

    public void setDealerEmployeeCell(String dealerEmployeeCell) {
        this.dealerEmployeeCell = dealerEmployeeCell;
    }

    public CustomerCarBean getCustomerCar() {
        return customerCar;
    }

    public void setCustomerCar(CustomerCarBean customerCar) {
        this.customerCar = customerCar;
    }

    public CarValuationBean getCarValuation() {
        return carValuation;
    }

    public void setCarValuation(CarValuationBean carValuation) {
        this.carValuation = carValuation;
    }

    public String getPickCarDate() {
        return pickCarDate;
    }

    public void setPickCarDate(String pickCarDate) {
        this.pickCarDate = pickCarDate;
    }
}
