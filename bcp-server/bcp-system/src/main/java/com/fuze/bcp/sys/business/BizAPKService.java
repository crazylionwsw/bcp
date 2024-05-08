package com.fuze.bcp.sys.business;

import com.fuze.bcp.api.sys.bean.APKReleaseBean;
import com.fuze.bcp.api.sys.bean.APKUpgradeLogBean;
import com.fuze.bcp.api.sys.service.IAPKBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.sys.domain.APKRelease;
import com.fuze.bcp.sys.domain.APKUpgradeLog;
import com.fuze.bcp.sys.service.IAPKReleaseService;
import com.fuze.bcp.sys.service.IAPKUpgradeLogService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class BizAPKService implements IAPKBizService {

    @Autowired
    IAPKReleaseService iapkReleaseService;

    @Autowired
    IAPKUpgradeLogService iapkUpgradeLogService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<APKReleaseBean> actSaveAPKRelease(APKReleaseBean apkRelease) {
        APKRelease apkRelease1 = iapkReleaseService.save(mappingService.map(apkRelease, APKRelease.class));
        if (apkRelease1 != null) {
            return ResultBean.getSucceed().setD(mappingService.map(apkRelease1, APKReleaseBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<APKReleaseBean> actDeleteAPKRelease(String apkReleaseId) {
        APKRelease apkRelease = iapkReleaseService.getOne(apkReleaseId);
        if (apkRelease != null) {
            apkRelease = iapkReleaseService.delete(apkReleaseId);
            if (apkRelease != null) {
                return ResultBean.getSucceed().setD(mappingService.map(apkRelease, APKReleaseBean.class));
            }
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<APKReleaseBean>> actGetAPKReleases(Integer currentPage) {
        Page<APKRelease> apkReleases = iapkReleaseService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(apkReleases, APKReleaseBean.class));
    }

    @Override
    public ResultBean<DataPageBean<APKReleaseBean>> actGetAPKReleases(Integer currentPage, @NotBlank Integer currentSize, String propName, Boolean isDesc) {
        if (currentPage == null) {
            currentPage = 0;
        }
        if (currentSize == null) {
            currentPage = 20;
        }
        if (propName == null) {
            propName = "ts";
        }
        if (isDesc == null) {
            isDesc = true;
        }
        Sort sort;
        if (isDesc) {
            sort = APKRelease.getSortDESC(propName);
        } else {
            sort = APKRelease.getSortASC(propName);
        }
        Pageable pageable = new PageRequest(currentPage, currentSize, sort);
        Page<APKRelease> apkReleases = iapkReleaseService.getAll(pageable);
        return ResultBean.getSucceed().setD(mappingService.map(apkReleases, APKReleaseBean.class));
    }

    @Override
    public ResultBean<APKReleaseBean> actGetLatestAPKRelease() {
        APKRelease apkRelease = iapkReleaseService.getAvaliableOneSortBy(APKRelease.getTsSort());
        if (apkRelease == null) {
            return ResultBean.getSucceed().setD(apkRelease);
        } else {
            return ResultBean.getSucceed().setD(mappingService.map(apkRelease, APKReleaseBean.class));
        }
    }

    @Override
    public ResultBean<DataPageBean<APKUpgradeLogBean>> actGetAPKUpgradeLogs(Integer currentPage) {
        Page<APKUpgradeLog> apkUpgradeLogs = iapkUpgradeLogService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(apkUpgradeLogs, APKUpgradeLogBean.class));
    }

    @Override
    public ResultBean<APKUpgradeLogBean> actSaveAPKUpgradeLog(APKUpgradeLogBean apkUpgradeLog) {
        APKUpgradeLog apkUpgradeLog1 = iapkUpgradeLogService.save(mappingService.map(apkUpgradeLog, APKUpgradeLog.class));
        if (apkUpgradeLog1 != null) {
            return ResultBean.getSucceed().setD(mappingService.map(apkUpgradeLog1, APKUpgradeLogBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<APKUpgradeLogBean> actDeleteAPKUpgradeLog(String apkUpgradeLogId) {

        APKUpgradeLog apkUpgradeLog = iapkUpgradeLogService.getOne(apkUpgradeLogId);
        if (apkUpgradeLog != null) {
            apkUpgradeLog = iapkUpgradeLogService.delete(apkUpgradeLogId);
            if (apkUpgradeLog != null) {
                return ResultBean.getSucceed().setD(mappingService.map(apkUpgradeLog, APKUpgradeLogBean.class));
            } else {
                return ResultBean.getSucceed();
            }
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actDeleteAPKUpgradeLog(List<APKUpgradeLogBean> apkUpgradeLogs) {
        if (apkUpgradeLogs != null && apkUpgradeLogs.size() > 0) {
            List<APKUpgradeLog> apkUpgradeLogs1 = mappingService.map(apkUpgradeLogs, APKUpgradeLog.class);
            iapkUpgradeLogService.deleteReal(apkUpgradeLogs1);
        }
        return ResultBean.getSucceed();
    }
}
