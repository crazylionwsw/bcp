package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.sys.bean.SysParamBean;
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
    private IParamBizService iParamBizService;

    /**
     * 获取单条系统参数
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/param/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSysParam(@PathVariable("id") String id) {
        return iParamBizService.actGetSysParam(id);
    }

    /**
     * 保存系统参数
     *
     * @param sysParam
     * @return
     */
    @RequestMapping(value = "/param", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveSysParam(@RequestBody SysParamBean sysParam) {
        return iParamBizService.actSaveSysParam(sysParam);
    }

    /**
     * 获取系统参数列表
     *
     * @return
     */
    @RequestMapping(value = "/params", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSysParams() {
        return iParamBizService.actGetSysParams();
    }

    /**
     * 删除系统参数
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/param/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteSysParam(@PathVariable("id") String id) {
        return iParamBizService.actDeleteSysParam(id);
    }

    /**
     * 通过编码获取配置（返回MAP）
     * @param code
     * @return
     */
    @RequestMapping(value="/param/map/{code}",method= RequestMethod.GET)
    public ResultBean getMapByCode(@PathVariable(value = "code")String code){
        return iParamBizService.actGetMap(code);
    }

    /**
     * 通过编码获取配置（返回List）
     * @param code
     * @return
     */
    @RequestMapping(value="/param/list/{code}",method= RequestMethod.GET)
    public ResultBean getListByCode(@PathVariable(value = "code")String code){
        return iParamBizService.actGetList(code);
    }
    /**
     * 通过编码获取配置（返回String）
     * @param code
     * @return
     */
    @RequestMapping(value="/param/string/{code}",method= RequestMethod.GET)
    public ResultBean getStringByCode(@PathVariable(value = "code")String code){
        return iParamBizService.actGetString(code);
    }
    /**
     * 通过编码获取配置（返回Integer）
     * @param code
     * @return
     */
    @RequestMapping(value="/param/int/{code}",method= RequestMethod.GET)
    public ResultBean getIntByCode(@PathVariable(value = "code")String code){
        return iParamBizService.actGetInteger(code);
    }

}
