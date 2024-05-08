package com.fuze.bcp.pad.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/3.
 */
@RestController
@RequestMapping(value = "/json")
public class SysParamController {

    @Autowired
    private IParamBizService iParamService;

    /**
     * 【PAD API】-- 获取系统客户手续费缴纳方式
     *
     * @param code code
     * @return
     */
    @RequestMapping(value = "/sysparam/{code}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSysParam(@PathVariable("code") String code) {
        ResultBean resultBean = iParamService.actGetString(code);
        if (resultBean.isSucceed()) {
            JSONArray object = JSONObject.parseObject((String) resultBean.getD(), JSONArray.class);
            JSONObject res = new JSONObject();
            res.put("code", code);
            res.put("paramValue", object);
            return ResultBean.getSucceed().setD(res);
        }
        return resultBean;
    }

    /**
     * 获取缴费单所需的收款账户
     */
    @RequestMapping(value = "/sysparam/{code}/account",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getParamAccount(@PathVariable("code") String code) throws Exception {
        return iParamService.actGetParamByCode(code);
    }

//    /**
//     * 获取单条系统参数
//     *
//     * @param id
//     * @return
//     */
//    @RequestMapping(value = "/param/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    public ResultBean getSysParam(@PathVariable("id") String id) {
//        return iParamService.actGetSysParam(id);
//    }
//
//    /**
//     * 保存系统参数
//     *
//     * @param sysParam
//     * @return
//     */
//    @RequestMapping(value = "/param", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultBean saveSysParam(@RequestBody SysParamBean sysParam) {
//        return iParamService.actSaveSysParam(sysParam);
//    }
//
//    /**
//     * 获取系统参数列表
//     *
//     * @return
//     */
//    @RequestMapping(value = "/params", method = RequestMethod.GET)
//    @ResponseBody
//    public ResultBean getSysParams() {
//        return iParamService.actGetSysParams();
//    }
//
//    /**
//     * 删除系统参数
//     *
//     * @param id
//     * @return
//     */
//    @RequestMapping(value = "/param/{id}", method = RequestMethod.DELETE)
//    @ResponseBody
//    public ResultBean deleteSysParam(@PathVariable("id") String id) {
//        return iParamService.actDeleteSysParam(id);
//    }
}
