package com.fuze.bcp.enterprise.controller;

import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Lily on 2017/12/29.
 */
@RestController
@RequestMapping(value = "/json", method = {RequestMethod.GET, RequestMethod.POST})
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

    /**
     * 根据员工ID获取分期经理
     * @param id    员工id
     * @return
     */
    @RequestMapping(value = "/employee/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<EmployeeBean> getStageManagerById(@PathVariable("id") String id){
        return iOrgBizService.actGetEmployee(id);
    }
}
