package com.fuze.bcp.api.sys.service;

import com.fuze.bcp.api.sys.bean.APKReleaseBean;
import com.fuze.bcp.api.sys.bean.APKUpgradeLogBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * PAD端APP管理
 */
public interface IAPKBizService {
    /**
     * 保存APK发布包
     *
     * @param apkRelease
     * @return
     */
    ResultBean<APKReleaseBean> actSaveAPKRelease(APKReleaseBean apkRelease);

    /**
     * 删除APK发布包
     *
     * @param apkReleaseId
     * @return
     */
    ResultBean<APKReleaseBean> actDeleteAPKRelease(@NotNull String apkReleaseId);

    /**
     * 获取发布包列表（带分页）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<APKReleaseBean>> actGetAPKReleases(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取发布包列表（带分页）
     *
     * @param currentPage  页数
     * @param currentSize  个数
     * @param propName     排序字段名称
     * @param isDesc       排序方式
     * @return
     */
    ResultBean<DataPageBean<APKReleaseBean>> actGetAPKReleases(Integer currentPage,Integer currentSize,String propName,Boolean isDesc);

    /**
     * 获取最新的发布版本
     * @return
     */
    ResultBean<APKReleaseBean> actGetLatestAPKRelease();


    /**
     * 获取APK更新日志列表（带分页）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<APKUpgradeLogBean>> actGetAPKUpgradeLogs(@NotNull @Min(0) Integer currentPage);

    /**
     * 添加APK更新日志
     *
     * @param apkUpgradeLog
     * @return
     */
    ResultBean<APKUpgradeLogBean> actSaveAPKUpgradeLog(APKUpgradeLogBean apkUpgradeLog);

    /**
     * 删除APK更新日志
     *
     * @param apkUpgradeLogId
     * @return
     */
    ResultBean<APKUpgradeLogBean> actDeleteAPKUpgradeLog(@NotNull String apkUpgradeLogId);

    /**
     * 删除APK更新日志
     *
     * @param apkUpgradeLogs
     * @return
     */
    ResultBean actDeleteAPKUpgradeLog(@NotNull List<APKUpgradeLogBean> apkUpgradeLogs);
}
