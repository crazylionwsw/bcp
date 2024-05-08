package com.fuze.bcp.app.controller;

import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/12/29.
 */
@RestController
@RequestMapping(value = "/json/app", method = {RequestMethod.GET, RequestMethod.POST})
public class OrgController {

    @Autowired
    IOrgBizService iOrgBizService;

    /**
     * 获取分期经理
     * @return
     */
    @RequestMapping(value = "/employee/businessmanager",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<EmployeeBean>> getStageManager(){
        return iOrgBizService.actGetBusinessManagers();
    }
}
