package com.fuze.bcp.app.controller;

import com.fuze.bcp.api.workflow.bean.TaskBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/12/25.
 */
@RestController
@RequestMapping(value = "/json/app")
public class WorkFlowController {

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    /**
     * 获取当前任务
     * @param businessKey
     * @return
     */
    @RequestMapping(value = "workflow/currentgroup", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<Map<String,String>> getTaskCurrentGroup(@RequestParam(value = "businessKey") String businessKey) {
        List<TaskBean> taskBeen = iWorkflowBizService.actGetBillTasks(businessKey).getD();
        Map<String,String> data = new HashMap<String,String>();
        if (taskBeen != null && taskBeen.size() > 0) {
            data.put("id",businessKey);
            data.put("name",taskBeen.get(0).getTaskDefinitionKey());
            return ResultBean.getSucceed().setD(data);
        }else{
            data.put("id",businessKey);
            return ResultBean.getSucceed().setD(data).setM("当前任务为空!");
        }
    }
}
