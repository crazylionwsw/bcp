package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.mq.bean.DynamicDescribeBean;
import com.fuze.bcp.api.mq.bean.TaskDescribeBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(value = "/taskdescribe/{id}", method = RequestMethod.GET)
    public ResultBean getTaskDescribe(@PathVariable("id") String id) {
        return iAmqpBizService.actGetTaskDescribe(id);
    }

    /**
     * 一级节点删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/taskdescribe/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteTaskDescribe(@PathVariable("id") String id) {
        return iAmqpBizService.actDeleteTaskDescribe(id);
    }

    @RequestMapping(value = "/taskdescribe/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveTaskDescribe(@RequestBody TaskDescribeBean taskDescribeBean) {
        return iAmqpBizService.actSaveTaskDescribe(taskDescribeBean);
    }

    @RequestMapping(value = "/taskdescribe/dynamic/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveDynamicDescribe(@RequestBody DynamicDescribeBean dynamicDescribeBean,  String taskDescId,  String parentId) {
        return iAmqpBizService.actSaveDynamicDescribe(dynamicDescribeBean, taskDescId, parentId);
    }

    /**
     * 二级节点及以下的删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/taskdescribe/dynamic/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteDynamicDescribe(@PathVariable("id") String id) {
        return iAmqpBizService.actDeleteDynamicDescribe(id);
    }

    @RequestMapping(value = "/taskdescribe/page", method = RequestMethod.GET)
    public ResultBean getTaskDescribes(@RequestParam(value = "noDunamic", defaultValue = "true") Boolean noDunamic, @RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iAmqpBizService.actGetTaskDescribes(currentPage, noDunamic);
    }

    @RequestMapping(value = "/taskdescribe/{taskdescribeId}/dynamicBeans", method = RequestMethod.GET)
    public ResultBean getDynamicBeans(@PathVariable("taskdescribeId") String taskdescribeId) {
        return iAmqpBizService.actGetDynamicDescribeBeansByTaskDescribeId(taskdescribeId);
    }

    @RequestMapping(value = "/taskrecord/page", method = RequestMethod.GET)
    public ResultBean getTaskRecords(@RequestParam(value = "currentPage", defaultValue = "0")Integer currentPage) {
        return iAmqpBizService.actGetTaskRecords(currentPage);
    }

    @RequestMapping(value = "/taskrecord/{id}", method = RequestMethod.GET)
    public ResultBean getTaskRecord(@PathVariable("id") String id) {
        return iAmqpBizService.actGetTaskRecord(id);
    }

    @RequestMapping(value = "/msg/record/{id}", method = RequestMethod.GET)
    public ResultBean getMsgRecord(@PathVariable("id") String id) {
        return iAmqpBizService.actGetMsgRecord(id);
    }

    @RequestMapping(value = "/msg/records/{ids}", method = RequestMethod.GET)
    public ResultBean getMsgRecords(@PathVariable("ids") List<String> ids) {
        return iAmqpBizService.actGetMsgRecords(ids);
    }

    //通知发送
    @RequestMapping(value = "/notice/send/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean sendNotice(@PathVariable("id") String id){
        return iAmqpBizService.actSendNotice(id);
    }


}
