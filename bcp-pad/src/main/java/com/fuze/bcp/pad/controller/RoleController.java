package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.auth.bean.SysRoleBean;
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
public class RoleController {

    @Autowired
    private IAuthenticationBizService iAuthenticationBizService;

    /**
     * 获取系统角色列表(分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSysRoles(@RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage) {
        return iAuthenticationBizService.actGetSysRoles(currentPage);
    }

    /**
     * 添加、修改系统角色
     *
     * @param sysRole
     * @return
     */
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveSysRole(@RequestBody SysRoleBean sysRole) {
        return iAuthenticationBizService.actSaveSysRole(sysRole);
    }

    /**
     * 获取系统角色下拉框(仅可用数据)
     *
     * @return
     */
    @RequestMapping(value = "/roles/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupSysRoles() {
        return iAuthenticationBizService.actLookupSysRoles();
    }

    /**
     * 根据id获取角色对象
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getRole(@PathVariable("id") String id) {
        return iAuthenticationBizService.actGetSysRole(id);
    }

    /**
     * 根据id删除系统角色
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/role/{id}")
    @ResponseBody
    public ResultBean deleteRole(@PathVariable("id") String id) {
        return iAuthenticationBizService.actDeleteSysRole(id);
    }

    /**
     * 根据多个ID获取多个对象
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/roles/pickup", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getSysRolesByIds(@RequestBody List<String> ids) {
        return iAuthenticationBizService.actGetSysRolesByIds(ids);
    }
}
