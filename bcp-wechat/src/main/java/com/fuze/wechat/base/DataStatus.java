package com.fuze.wechat.base;

public interface DataStatus {

    /**
     * 暂存状态
     */
    Integer TEMPSAVE = 0;

    /**
     * 数据保存状态
     */
    Integer SAVE = 1;

    /**
     * 数据锁定状态
     */
    Integer LOCK = 8;

    /**
     * 数据作废状态
     */
    Integer DISCARD = 9;

}
