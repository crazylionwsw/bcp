package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 车型信息业务对象
 */
@Data
@MongoEntity(entityName = "bd_cartype")
public class CarTypeBean extends APIBaseDataBean {

    //汽油
    public final static Integer MOTIVE_TYPE_FUEL = 0;
    //柴油
    public final static Integer MOTIVE_TYPE_DIESEL = 1;
    //混合动力
    public final static Integer MOTIVE_TYPE_HYBIRD = 2;
    //电池
    public final static Integer MOTIVE_TYPE_POWER = 3;



    /**
     * 动力类型
     */
    private Integer motiveType = MOTIVE_TYPE_FUEL;


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
     * GroupName
     */
    private String groupName;

    /**
     * 座位数
     */
    private Integer seat;

    /**
     * 颜色
     */
    private List<String> colors = new ArrayList<String>();

    /**
     * 官方价格
     */
    private Double price;

    /**
     * 图片信息
     */
    private String imageFileId = null;

    /**
     * 品牌
     */
    private String carBrandId = null;

    /**
     * 车系
     */
    private String carModelId = null;

    /**
     * 全称（品牌名+车系名+车型名）
     */
    private String fullName = null;

    /**
     * 厂牌型号
     * TODO
     */
    private String factoryType = null;

    /**
     * 年款
     */
    private String year;

    /**
     * 上市时间
     */
    private String marketDate = null;

}
