package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.EmployeeLookupBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.workflow.bean.*;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ResultBean;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/27.
 */
@RestController
@RequestMapping(value = "/json/workflow")
public class WorkflowController extends BaseController {

    @Autowired
    private IWorkflowBizService iWorkflowBizService;

    @Autowired
    private IOrgBizService iOrgBizService;


    /**
     * 获取全部工作流角色
     *
     * @return
     */
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<GroupBean>> getGroups() {
        return iWorkflowBizService.actGetAllGroups();
    }

    /**
     * 给用户分配工作流角色
     *
     * @return
     */
    @RequestMapping(value = "/user/{username}/groups", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean assignGroups(@PathVariable("username") String username, @RequestBody List<String> groupIds) {
        return iWorkflowBizService.actAssignUserGroups(username, groupIds);
    }


    /**
     * 获取用户工作流角色
     *
     * @return
     */
    @RequestMapping(value = "/user/{username}/groups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<GroupBean>> getUserGroups(@PathVariable("username") String username) {
        return iWorkflowBizService.actGetUserGroups(username);
    }

    /**
     * PAD端提交备注信息接口
     *
     * @return
     */
    @RequestMapping(value = "/signinfo/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean postSignInfo(@PathVariable("id") String id, @RequestBody String body) {
        JSONObject object = new JSONObject(body);
        String comment = (String) object.get("comment");
        SignInfo signInfo = new SignInfo();
        signInfo.setUserId(super.getOperatorId());
        EmployeeLookupBean employee = iOrgBizService.actGetEmployeeByLogin(super.getOperatorId()).getD();
        signInfo.setEmployeeId(employee != null ? employee.getId() : null);
        signInfo.setComment(comment);
        signInfo.setResult(0);
        signInfo.setFlag(0);

        return iWorkflowBizService.actSaveWorkflowBill(id, signInfo, 1);
    }

    /**
     * 获取签批信息
     *
     * @return
     */
    @RequestMapping(value = "/signinfos", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<SignInfoBean>> getSignInfoList(@RequestParam("id") String id) {
        WorkFlowBillBean workFlowBill = iWorkflowBizService.actGetBillWorkflowTEM(id).getD();
        if (workFlowBill == null) {
            return ResultBean.getFailed().setM("该单据未进入工作流，暂无签批信息！");
        }
        List<TEMSignInfo> signInfos = workFlowBill.getSignInfos();
        List<SignInfoBean> signInfoList = new ArrayList<SignInfoBean>();
        if (signInfos != null) {
            for (TEMSignInfo signinfo : signInfos) {
                SignInfoBean signInfoBean = new SignInfoBean();
                signInfoBean.setUserId(signinfo.getUserId());
                signInfoBean.setEmployeeId(signinfo.getEmployeeId());

                //获取员工信息
                if (signinfo.getEmployeeId() != null) {
                    EmployeeBean employee = iOrgBizService.actGetEmployee(signinfo.getEmployeeId()).getD();
                    signInfoBean.setEmployeeName(employee.getUsername());
                    signInfoBean.setEmployeeAvatar(employee.getAvatarFileId());
                }
                signInfoBean.setResult(signinfo.getResult());
                signInfoBean.setFlag(signinfo.getFlag() == null ? SignInfo.FLAG_COMMENT : signinfo.getFlag());
                signInfoBean.setComment(signinfo.getComment());
                signInfoBean.setTs(signinfo.getTs());
                signInfoBean.setAuditStatus(signinfo.getAuditStatus());
                signInfoBean.setTaskInfo(signinfo.getTaskInfo());
                signInfoList.add(signInfoBean);
            }
        }
        if (signInfoList == null || signInfoList.isEmpty()) {
            return ResultBean.getFailed().setM("暂无签批信息");
        } else {
            return ResultBean.getSucceed().setD(signInfoList);
        }
    }
}
