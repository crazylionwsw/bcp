package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.sys.bean.APKReleaseBean;
import com.fuze.bcp.api.sys.service.IAPKBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by user on 2017/7/3.
 */
@RestController
@RequestMapping(value = "/json")
public class ApkVersionController {

    @Autowired
    private IAPKBizService iAPKBizService;

    /**
     * 【PAD API】-- 检查新版本
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/version/lastest", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getLastVersion(HttpServletRequest request) {
        ResultBean resultBean = iAPKBizService.actGetLatestAPKRelease();
        if (resultBean.isSucceed()) {
            String urlPath = String.format("http://%s:%s", request.getServerName(), request.getServerPort());
            APKReleaseBean releaseBean = (APKReleaseBean) resultBean.getD();
            if (releaseBean != null){
                releaseBean.setApkUrl(urlPath + "/json/file/download/" + releaseBean.getApkUrl());
                return resultBean.setD(releaseBean);
            }
        }
        return resultBean;
    }
}
