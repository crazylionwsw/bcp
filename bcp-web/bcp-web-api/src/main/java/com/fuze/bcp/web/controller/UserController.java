package com.fuze.bcp.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by admin on 2017/5/27.
 */
@RestController
@RequestMapping(value = "/json")
public class UserController extends BaseController {

    @Autowired
    private IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    private IWorkflowBizService iWorkflowBizService;

    /**
     * 获取用户列表（带分页）
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResultBean getUsers(@RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage) {
        return iAuthenticationBizService.actGetLoginUsers(currentPage);
    }
    @RequestMapping(value = "/user/lookup",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupLoginUsers(){
        return iAuthenticationBizService.actLookupLoginUser();
    }

    /**
     * 模糊查询登录用户
     */
    @RequestMapping(value = "/users/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getByUsername(@RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage,@RequestBody LoginUserBean loginUserBean) {
        return iAuthenticationBizService.actSearchLoginUsers(currentPage,loginUserBean);
    }


    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean removeUser(@PathVariable("id") String id) {
        ResultBean R= iAuthenticationBizService.actDeleteLoginUser(id);
        return R;
    }

    /**
     * 获取单个用户数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/user/{id}")
    @ResponseBody
    public ResultBean getUser(@PathVariable("id") String id) {
        return iAuthenticationBizService.actGetLoginUser(id);
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveUser(@RequestBody LoginUserBean user) {
        ResultBean<LoginUserBean> result = iAuthenticationBizService.actSaveLoginUser(user);
        user = result.getD();
        iWorkflowBizService.actAssignUserGroups(user.getActivitiUserId(), user.getActivitiUserRoles());
        return result;
    }


    /**
     * 恢复用户
     */
    @RequestMapping(value = "/renew/user",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean renewUser(@RequestBody LoginUserBean user){
        return iAuthenticationBizService.actRenewLoginUser(user);
    }

}
