package com.fuze.bcp.app.controller;

import com.fuze.bcp.api.auth.bean.LoginBean;
import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.auth.service.ILoginTokenBizService;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.UserProfileBean;
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
import java.util.Map;


/**
 * 登录接口
 * Created by 王少伟 on 2011/1/12.
 */

@RestController
@RequestMapping(value = "/json/app")
public class AppLoginControllerJson {

    private static Logger logger = LoggerFactory.getLogger(AppLoginControllerJson.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ILoginTokenBizService iLoginTokenService;

    @Autowired
    private IUserProfileBizService iUserProfileBizService;

    @Autowired
    IWechatBizService iWechatBizService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    /**
     * 用户名、密码登录
     *
     * @param login
     * @param device
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "使用用户名和密码登录")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "LoginUserBean")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultBean login(@RequestBody LoginBean login, Device device, HttpServletRequest req, HttpServletResponse response) throws AuthenticationException, IOException {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login.getUsername(),
                            login.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(login.getUsername());

            //获取客户端设备类型
            String clientType = "APP";

            String clientIp = NetworkUtil.getIpAddress(req);

            String token = null;
            try {
                String oldToken = iLoginTokenService.getLoginTokenInfo(jwtUser.getUsername(), clientType);
                if (oldToken != null) {
                    token = iLoginTokenService.refreshLoginTokenInfo(jwtUser.getUsername(), clientType);
                } else {
                    token = iLoginTokenService.setLoginTokenInfo(jwtUser.getUsername(), clientType);
                }
                UserProfileBean profile = iUserProfileBizService.getProfile(jwtUser.getId()).getD();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("token", token);
                map.put("userId", jwtUser.getId());
                map.put("userName", jwtUser.getUsername());
                map.put("isSystem", jwtUser.getIsSystem());

                EmployeeBean employeeBean = iOrgBizService.actFindEmployeeByLoginUserId(jwtUser.getId()).getD();
                if (employeeBean != null) {
                    map.put("displayName", employeeBean.getUsername());
                }
                if (profile != null) {
                    map.put("avatarFileId", profile.getAvatarFileId());
                }
                response.setHeader("Login-State", "status-success");
                return ResultBean.getSucceed().setD(map);
            } catch (InterruptedException ex) {
                response.setHeader("Login-State", "status-error");
                return ResultBean.getFailed().setM(ex.getLocalizedMessage());

            }
        } catch (AuthenticationException ex) {
            response.setHeader("Login-State", "status-error");
            return ResultBean.getFailed().setM(ex.getLocalizedMessage());
        }
    }

    @RequestMapping(value = "/wechatlogin", method = RequestMethod.GET)
    public ResultBean loginWc(@RequestParam String code, Device device, HttpServletRequest req, HttpServletResponse response) throws IOException {
        AccessToken accessToken = iWechatBizService.accessToken().getD();
        if (accessToken != null && accessToken.getAccess_token() != null) {
            String userId = iWechatBizService.getMemberGuidByCode(accessToken.getAccess_token(), code, iWechatBizService.getAgentId()).getD();
            if (userId == null) {
                userId = iWechatBizService.getMemberGuidByCode(iWechatBizService.accessToken(true).getD().getAccess_token(), code, iWechatBizService.getAgentId()).getD();
            }
            ResultBean<EmployeeBean> resultBean = iOrgBizService.actGetEmployeeByWcUserId(userId);
            if (resultBean.isSucceed()) {
                EmployeeBean employeeBean = resultBean.getD();
                String loginUserId = employeeBean.getLoginUserId();
                LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(loginUserId).getD();
                if (loginUserBean != null) {
                    try {
                        //获取客户端设备类型
                        String clientType = "APP";
                        String clientIp = NetworkUtil.getIpAddress(req);
                        String token = null;
                        try {
                            String oldToken = iLoginTokenService.getLoginTokenInfo(loginUserBean.getUsername(), clientType);
                            if (oldToken != null) {
                                token = iLoginTokenService.refreshLoginTokenInfo(loginUserBean.getUsername(), clientType);
                            } else {
                                token = iLoginTokenService.setLoginTokenInfo(loginUserBean.getUsername(), clientType);
                            }
                            UserProfileBean profile = iUserProfileBizService.getProfile(loginUserBean.getId()).getD();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("token", token);
                            map.put("userId", loginUserBean.getId());
                            map.put("userName", loginUserBean.getUsername());
                            map.put("displayName", employeeBean.getUsername());
                            map.put("isSystem", loginUserBean.getSystem());
                            if (profile != null) {
                                map.put("avatarFileId", profile.getAvatarFileId());
                            }
                            response.setHeader("Login-State", "status-success");
                            return ResultBean.getSucceed().setD(map);
                        } catch (InterruptedException ex) {
                            response.setHeader("Login-State", "status-error");
                            return ResultBean.getFailed().setM(ex.getLocalizedMessage());
                        }
                    } catch (AuthenticationException ex) {
                        response.setHeader("Login-State", "status-error");
                        return ResultBean.getFailed().setM(ex.getLocalizedMessage());
                    }
                } else {
                    return ResultBean.getFailed().setM("登录失败，系统用户错误，请联系管理员");
                }
            } else {
                return ResultBean.getFailed().setM("登录失败，该微信用户不是系统用户");
            }
        }
        return ResultBean.getFailed().setM("网络连接超时，请稍后重试");
    }

    /**
     * 退出登录，清除用户的token
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResultBean loginOut(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = request.getHeader("User-Token");
            iLoginTokenService.clearLoginToken(token);
            return ResultBean.getSucceed().setM("LOGOUT_SUCCESS");
        } catch (Exception e) {
            return ResultBean.getFailed().setM("LOGOUT_ERROR");
        }
    }

    /**
     * 获取验证码
     * @param cell
     *//*
    @RequestMapping(value = "/verifycode/{cell}", method = RequestMethod.GET)
    @ResponseBody
    public ResultMsg getCellCode(@PathVariable(value = "cell") String cell) {
        return iAuthenticationsService.getCellValidateCode(cell);
    }

    *//**
     * 修改密码
     *//*
    @RequestMapping(value = "/login/password", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg<LoginUser> updatePassword(@RequestBody LoginUser loginUser){
        ResultMsg<LoginUser> resultMsg = iAuthenticationsService.updatePassword(loginUser);
        return resultMsg;
    }*/


    /**
     * 手机号 和 验证码登录
     * @param user
     * @param user
     * @TODO
     */
//    @RequestMapping(value = "/login/cell", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultMsg loginByCell(@RequestBody LoginUser user, HttpServletRequest request, HttpServletResponse response) {
//        if(iAuthenticationsService.isCellExist(user.getUsername())){
//            // 根据验证码进行登录，创建token返回到客户端
//            return createLoginToken(user,request,response);
//        }else{
//            return ResultMsg.getFailed().setM("手机号码不存在!");
//        }
//    }


}
