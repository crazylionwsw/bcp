package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.sys.bean.APKReleaseBean;
import com.fuze.bcp.api.sys.bean.APKUpgradeLogBean;
import com.fuze.bcp.api.sys.service.IAPKBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/3.
 */
@RestController
@RequestMapping(value = "/json")
public class ApkVersionController {

    @Autowired
    private IAPKBizService iapkBizService;

    /**
     * 保存APK发布包
     *
     * @param apkRelease
     * @return
     */
    @RequestMapping(value = "/apkversion", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveAPKRelease(@RequestBody APKReleaseBean apkRelease) {
        return iapkBizService.actSaveAPKRelease(apkRelease);
    }

    /**
     * 删除APK发布包
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/apkversion/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteAPKRelease(@PathVariable("id") String id) {
        return iapkBizService.actDeleteAPKRelease(id);
    }

    /**
     * 获取APK发布包列表(带分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/apkversions", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAPKReleases(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iapkBizService.actGetAPKReleases(currentPage);
    }

    /**
     * 获取APK发布包更新日志列表(带分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/apkversion/upgrade/logs", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAPKUpgradeLogs(@RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage) {
        return iapkBizService.actGetAPKUpgradeLogs(currentPage);
    }

    /**
     * 保存APK发布包更新日志
     *
     * @param apkUpgradeLog
     * @return
     */
    @RequestMapping(value = "/apkversion/upgrade/log", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveAPKUpgradeLog(@RequestBody APKUpgradeLogBean apkUpgradeLog) {
        return iapkBizService.actSaveAPKUpgradeLog(apkUpgradeLog);
    }

    /**
     * 删除APK发布包更新日志
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/apkversion/upgrade/log/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteAPKUpgradeLogBean(String id) {
        return iapkBizService.actDeleteAPKUpgradeLog(id);
    }

}
