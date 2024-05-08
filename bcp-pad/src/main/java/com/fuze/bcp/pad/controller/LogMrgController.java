package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.sys.bean.LoginLogBean;
import com.fuze.bcp.api.sys.service.ILogBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/1.
 */
@RestController
@RequestMapping(value = "/json")
public class LogMrgController {

//    private ILogBizService iLogBizService;
//
//    /**
//     * 日志记录(带分页)
//     * @param currentPage
//     * @return
//     */
//    @RequestMapping(value = "/loginlogs/page",method = RequestMethod.GET)
//    @ResponseBody
//    public ResultBean actGetLoginLogs(@RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage){
//        return iLogBizService.actGetLoginLogs(currentPage);
//    }
//
//    /**
//     * 获取登录日志记录
//     * @return
//     */
//    @RequestMapping(value = "/loginlogs",method = RequestMethod.GET)
//    @ResponseBody
//    public ResultBean actGetLoginLogs(){
//        return iLogBizService.actGetLoginLogs();
//    }
//
//    /**
//     * 获取单条记录
//     * @param id
//     * @return
//     */
//    @RequestMapping(value = "/loginlog/{id}",method = RequestMethod.GET)
//    @ResponseBody
//    public ResultBean actGetLoginLog(@PathVariable("id") String id){
//        return iLogBizService.actDeleteLoginLogs(id);
//    }
//
//    /**
//     * 保存日志
//     * @return
//     */
//    @RequestMapping(value = "/loginlog",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultBean actSaveLoginLogs(@RequestBody LoginLogBean loginLog){
//        return iLogBizService.actSaveLoginLogs(loginLog);
//    }
//
//    /**
//     * 删除日志记录
//     * @return
//     */
//    @RequestMapping(value = "/loginlog/{id}",method = RequestMethod.DELETE)
//    @ResponseBody
//    public ResultBean actDeleteLoginLogs(@PathVariable("id") String id){
//        return iLogBizService.actDeleteLoginLogs(id);
//    }
//
//    /**
//     * 获取日志记录(仅可用)
//     * @return
//     */
//    @RequestMapping(value = "/loginlogs/lookups",method = RequestMethod.GET)
//    @ResponseBody
//    public ResultBean actLookupLoginLogs(){
//        return iLogBizService.actLookupLoginLogs();
//    }
}
