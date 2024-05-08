package com.fuze.bcp.enterprise.controller;

import com.fuze.bcp.api.auth.bean.LoginBean;
import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.bean.SysResourceBean;
import com.fuze.bcp.api.auth.jwt.JwtTokenUtil;
import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.auth.service.ILoginTokenBizService;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.bd.service.IUserProfileBizService;
import com.fuze.bcp.api.wechat.bean.AccessToken;
import com.fuze.bcp.api.wechat.service.IWechatBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.utils.NetworkUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017-05-30.
 */
@RestController
@RequestMapping(value = "/json")
public class AuthController extends BaseController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    private IUserProfileBizService iUserProfileBizService;

    @Autowired
    private ILoginTokenBizService iLoginTokenBizService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IWechatBizService iWechatBizService;

    /**
     * 根据code获取Userid后跳转到需要带用户信息的最终页面
     *
     * @param request
     * @param code      获取微信重定向到自己设置的URL中code参数
     * @param oauth2url 跳转到最终页面的地址
     * @return
     */
    @RequestMapping(value = "/oauth2url/{code}", method = RequestMethod.GET)
    public ResultBean Oauth2MeUrl(HttpServletRequest request, @PathVariable("code") String code, String oauth2url) throws IOException {
        logger.info("code: " + code);
        logger.info("oauth2url: " + oauth2url);
        AccessToken accessToken = iWechatBizService.accessToken().getD();
        if (accessToken != null && accessToken.getAccess_token() != null) {
            String userId = iWechatBizService.getMemberGuidByCode(accessToken.getAccess_token(), code, iWechatBizService.getAgentId()).getD();
            if (userId == null) {
                userId = iWechatBizService.getMemberGuidByCode(iWechatBizService.accessToken(true).getD().getAccess_token(), code, iWechatBizService.getAgentId()).getD();
            }
            ResultBean<EmployeeBean> resultBean = iOrgBizService.actGetEmployeeByWcUserId(userId);
            logger.info("----------" + "UserId: " + userId + "----------");
            if (resultBean.isSucceed() && resultBean.getD() != null) { //获取客户端设备类型
                EmployeeBean employeeBean = resultBean.getD();
                String loginUserId = employeeBean.getLoginUserId();
                LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(loginUserId).getD();
                if (loginUserBean != null) {
                    String clientType = "APP";
                    //Redis保存Token
                    String clientIP = NetworkUtil.getIpAddress(request);

                    String token = null;
                    try {
                        String oldToken = iLoginTokenBizService.getLoginTokenInfo(loginUserBean.getUsername(), clientType);
                        if (oldToken != null) {
                            token = iLoginTokenBizService.refreshLoginTokenInfo(loginUserBean.getUsername(), clientType);
                        } else {
                            token = iLoginTokenBizService.setLoginTokenInfo(loginUserBean.getUsername(), clientType);
                        }
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("token", token);
                        map.put("userID", loginUserBean.getId());
                        map.put("userName", loginUserBean.getUsername());
                        map.put("isSystem", loginUserBean.getSystem());
                        //获取用户信息
                        map.put("employee", iUserProfileBizService.getProfile(loginUserId).getD());
                        //获取用户权限
                        map.put("permissions", iAuthenticationBizService.actGetUserSysResources(loginUserId).getD());
                        //返回token及用户信息
                        return ResultBean.getSucceed().setD(map);
                    } catch (Exception ex) {
                        return ResultBean.getFailed().setM(ex.getLocalizedMessage());
                    }
                }
            }
        }
        return ResultBean.getFailed().setM("当前登录的微信用户没有绑定员工信息！");
    }

    /**
     * 登录
     *
     * @param login
     * @param device
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "使用用户名和密码登录")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "LoginUserBean")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultBean login(@RequestBody LoginBean login, Device device, HttpServletRequest req) throws AuthenticationException, IOException {
        try {
            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login.getUsername(),
                            login.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload password post-security so we can generate token
            final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(login.getUsername());

            //获取客户端设备类型
            String clientType = "PC";

            //Redis保存Token
            String clientIP = NetworkUtil.getIpAddress(req);

            String token = null;
            try {
                String oldToken = iLoginTokenBizService.getLoginTokenInfo(jwtUser.getUsername(), clientType);
                if (oldToken != null) {
                    token = iLoginTokenBizService.refreshLoginTokenInfo(jwtUser.getUsername(), clientType);
                } else {
                    token = iLoginTokenBizService.setLoginTokenInfo(jwtUser.getUsername(), clientType);
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("token", token);
                map.put("userID", jwtUser.getId());
                map.put("userName", jwtUser.getUsername());
                map.put("isSystem", jwtUser.getIsSystem());

                //获取用户信息
                map.put("employee", iUserProfileBizService.getProfile(jwtUser.getId()).getD());
                //获取用户权限
                map.put("permissions", iAuthenticationBizService.actGetUserSysResources(jwtUser.getId()).getD());

                //返回token及用户信息
                return ResultBean.getSucceed().setD(map);

            } catch (Exception ex) {
                return ResultBean.getFailed().setM("用户名或密码错误!");
            }

        } catch (AuthenticationException ex) {
            return ResultBean.getFailed().setM("用户名或密码错误!");
        }
    }

    /**
     * 验证token的有效性
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/token/{token}/validation", method = RequestMethod.GET)
    public ResultBean validateToken(@PathVariable("token") String token, HttpServletRequest req) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String username = jwtTokenUtil.getUsernameFromToken(token);

        if (username != null) {
            String existToken = iLoginTokenBizService.getLoginTokenInfo(username, "PC");
            if (existToken != null) {
                if (existToken.equals(token)) {
                    return ResultBean.getSucceed();
                } else {
                    return ResultBean.getFailed().setM("Token失效！");
                }
            } else {
                return ResultBean.getFailed().setM("Token失效！");
            }
        } else {
            return ResultBean.getFailed().setM("无效的Token！");
        }
    }

    /**
     * 退出
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResultBean loginOut(HttpServletRequest request, HttpServletResponse response) {
        try {

            String token = request.getHeader("User-Token");
            iLoginTokenBizService.clearLoginToken(token);
            return ResultBean.getSucceed().setM("LOGOUT_SUCCESS");
        } catch (Exception e) {
            return ResultBean.getFailed().setM("LOGOUT_ERROR");
        }
    }

    /**
     * 获取用户的权限列表
     *
     * @return
     */
    @RequestMapping(value = "/user/{id}/permissions", method = RequestMethod.GET)
    public ResultBean<List<SysResourceBean>> getUserPermissions(@PathVariable("id") String id) {
        return iAuthenticationBizService.actGetUserSysResources(id);
    }

    /**
     * 获取员工资料
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/{id}/profile", method = RequestMethod.GET)
    public ResultBean getUserProfile(@PathVariable("id") String id) {
        return iUserProfileBizService.getProfile(id);
    }

    /**
     * 获取当前登录用户的审批权限
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/{id}/approval", method = RequestMethod.GET)
    public ResultBean getLoginUserApproval(@PathVariable("id") String id) {
        return iUserProfileBizService.actGetUserApproval(id);
    }

    /**
     *获取当前登陆人数
     */
    @RequestMapping(value = "/loguser/count",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getLogCount(){
        return iAuthenticationBizService.actGetLogUpCount();
    }

    /**
     *获取当前登陆人数
     */
    @RequestMapping(value = "/logmanagement/offline/{logName}/{logType}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean downLoguser(@PathVariable("logName") String logName,@PathVariable("logType") String logType){
        return iAuthenticationBizService.actOffLine(logName,logType);
    }

}