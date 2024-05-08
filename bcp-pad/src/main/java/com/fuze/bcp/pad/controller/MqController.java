package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.mq.bean.TaskDescribeBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

/**
 * Created by CJ on 2017/6/19.
 */
@RestController
@RequestMapping(value = "/json")
public class MqController {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @RequestMapping(value = "/taskTest", method = RequestMethod.GET)
    public ResultBean doTask() {
        Object[] p = new Object[]{"aaaaaaa"};
//        iAmqpBizService.actSendMsg("59671260d100750d74f77575", p);
        return ResultBean.getSucceed();
    }

    @RequestMapping(value = "/taskdescribe/{id}", method = RequestMethod.GET)
    public ResultBean getTaskDescribe(@PathVariable("id") String id) {
        return iAmqpBizService.actGetTaskDescribe(id);
    }

    @RequestMapping(value = "/taskdescribe/save", method = RequestMethod.POST)
    public ResultBean saveTaskDescribe(@RequestBody TaskDescribeBean taskDescribeBean) {
        iAmqpBizService.actSaveTaskDescribe(taskDescribeBean);
        return ResultBean.getSucceed();
    }

    @RequestMapping(value = "/taskdescribe/page", method = RequestMethod.GET)
    public ResultBean getTaskDescribes(Integer currentPage) {
        return iAmqpBizService.actGetTaskDescribes(currentPage,true);
    }

    @RequestMapping(value = "/taskrecord/page", method = RequestMethod.GET)
    public ResultBean getTaskRecords(Integer currentPage) {
        return iAmqpBizService.actGetTaskRecords(currentPage);
    }

    @RequestMapping(value = "/taskrecord/{id}", method = RequestMethod.GET)
    public ResultBean getTaskRecord(@PathVariable("id") String id) {
        return iAmqpBizService.actGetTaskRecord(id);
    }

}
