package com.fuze.wechat.controller;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.service.IEmployeeService;
import com.fuze.wechat.service.ISMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ZQW on 2018/5/2.
 */
@RestController
@RequestMapping(value = "/json")
public class EmployeeController {

    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    ISMSService ismsService;

    /**
     *  检查输入的手机号，是否为系统用户
     * @return
     */
    @RequestMapping(value = "/employee/check",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean checkEmployeeCell(@RequestParam(value = "cell") String cell){
        return iEmployeeService.actCheckEmployeeCell(cell);
    }

    /**
     *  将分期经理的手机号，绑定到系统用户中
     * @param cell
     * @return
     */
    @RequestMapping(value = "/employee/bind",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean bindEmployeeManager(@RequestParam(value = "cell") String cell, @RequestParam(value = "openId", required = false) String employeeOpenId, @RequestParam(value = "mpId", required = false) String employeeMpOpenId){
        return iEmployeeService.actBindEmployeeManager(cell, employeeOpenId, employeeMpOpenId);
    }

    @RequestMapping(value = "/code",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean sendCode(@RequestParam(value = "cell") String cell){
        return ismsService.sendCode(cell);
    }
}
