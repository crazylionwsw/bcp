package com.fuze.bcp.bd.domain;

import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by sean on 16/10/10.
 * c
 * 车型信息业务对象
 */
@Document(collection = "bd_cartype")
@Data
public class CarType extends BaseDataEntity {

    /**
     * 动力类型
     */
    private Integer motiveType = CarTypeBean.MOTIVE_TYPE_FUEL;


    /**
     * 车型编码
     */
    private String model;

    /**
     * 外部ID
     */
    private String refStyleId;

    /**
     * 排量
     */
    private String ml;

    /**
     * 座位数
     */
    private Integer seat;

    /**
     * 颜色
     */
    private List<String> colors;

    /**
     * 官方指导价
     */
    private Double price;

    /**
     * 图片信息
     */
    private String imageFileId = null;

    /**
     * GroupName
     */
    private String groupName;

    /**
     * 品牌
     */
    private String carBrandId;

    /**
     * 车系
     */
    private String carModelId;

    /**
     * 全称
     */
    private String fullName;

    /**
     * 年款
     */
    private String year;

    /**
     * 上市时间
     */
    private String marketDate = null;

    /**
     * 厂牌型号
     * TODO
     */
    private String factoryType = null;

    public Integer getMotiveType() {
        return motiveType;
    }

    public static Integer getMotiveTypeFuel() {
        return CarTypeBean.MOTIVE_TYPE_FUEL;
    }

    public static Integer getMotiveTypeDiesel() {
        return CarTypeBean.MOTIVE_TYPE_DIESEL;
    }

    public static Integer getMotiveTypeHybird() {
        return CarTypeBean.MOTIVE_TYPE_HYBIRD;
    }

    public static Integer getMotiveTypePower() {
        return CarTypeBean.MOTIVE_TYPE_POWER;
    }

    public String getModel() {
        return model;
    }

    public String getRefStyleId() {
        return refStyleId;
    }

    public String getMl() {
        return ml;
    }

    public Integer getSeat() {
        return seat;
    }

    public List<String> getColors() {
        return colors;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageFileId() {
        return imageFileId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCarBrandId() {
        return carBrandId;
    }

    public String getCarModelId() {
        return carModelId;
    }

    public String getFactoryType() {
        return factoryType;
    }

    public void setMotiveType(Integer motiveType) {
        this.motiveType = motiveType;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setRefStyleId(String refStyleId) {
        this.refStyleId = refStyleId;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setImageFileId(String imageFileId) {
        this.imageFileId = imageFileId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCarBrandId(String carBrandId) {
        this.carBrandId = carBrandId;
    }

    public void setCarModelId(String carModelId) {
        this.carModelId = carModelId;
    }

    public void setFactoryType(String factoryType) {
        this.factoryType = factoryType;
    }
}
