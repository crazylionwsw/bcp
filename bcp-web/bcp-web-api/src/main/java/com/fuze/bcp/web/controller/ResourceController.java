package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.bean.SysResourceBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by admin on 2017/5/27.
 */
@RestController
@RequestMapping(value = "/json")
public class ResourceController {

    @Autowired
    private IAuthenticationBizService iAuthenticationBizService;

    /**
     * 获取子节点
     *
     * @return
     */
    @RequestMapping(value = "/sysresource/{id}/sysresources", method = RequestMethod.GET)
    public ResultBean getSysResources(@PathVariable("id") String id) {
        ResultBean<List<SysResourceBean>> sysresources = iAuthenticationBizService.actGetChildSysResources(id);
        return sysresources;
    }

    /**
     * 保存系统资源
     *
     * @param sysResourceBean
     * @return
     */
    @RequestMapping(value = "/sysresource", method = RequestMethod.POST)
    @ResponseBody
    private ResultBean saveSysResource(@RequestBody SysResourceBean sysResourceBean) {
        ResultBean<SysResourceBean> sysResourceMsg = iAuthenticationBizService.actSaveSysResource(sysResourceBean);
        return sysResourceMsg;
    }

    /**
     * 根据ids列表获取系统资源
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/sysresources/pickup", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<SysResourceBean>> getSysRolesByIds(@RequestBody List<String> ids) {
        ResultBean<List<SysResourceBean>> resultMsg = iAuthenticationBizService.actGetSysResources(ids);
        return resultMsg;
    }

    /**
     * 作废参数，第二次是删除参数
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sysresource/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean<SysResourceBean> delete(@PathVariable("id") String id) {
        ResultBean<SysResourceBean> resultBean = iAuthenticationBizService.actDeleteSysResource(id);
        return resultBean;
    }

    /**
     * 获取资源下拉列表(仅可用数据)
     *
     * @return
     */
    @RequestMapping(value = "/sysresources/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupSysResources() {
        return iAuthenticationBizService.actLookupSysResources();
    }
}
