package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.bean.PadLoginBean;
import com.fuze.bcp.api.auth.bean.UserBean;
import com.fuze.bcp.api.auth.jwt.JwtTokenUtil;
import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.auth.service.ILoginTokenBizService;
import com.fuze.bcp.api.bd.bean.UserProfileBean;
import com.fuze.bcp.api.bd.service.IUserProfileBizService;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.api.sys.bean.TerminalBindBean;
import com.fuze.bcp.api.sys.bean.TerminalDeviceBean;
import com.fuze.bcp.api.sys.service.IDeviceBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.utils.StringHelper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017-05-30.
 */
@RestController
@RequestMapping(value = "/json")
public class AuthController extends BaseController {

    @Autowired
    private IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    private ILoginTokenBizService iLoginTokenBizService;

    @Autowired
    private IDeviceBizService IDeviceBizService;

    @Autowired
    private AuthenticationManager autshenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private IUserProfileBizService iUserProfileBizService;

    @Autowired
    private IMessageBizService jMessageBizService;


    /**
     * 登录
     *
     * @param login
     * @return ResultBean
     */
    @ApiOperation(value = "用户登录", notes = "使用用户名和密码登录")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "PadLoginBean")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultBean login(@RequestBody PadLoginBean login, HttpServletRequest req, @RequestHeader("deviceType") String deviceType, @RequestHeader("OS") String os) throws AuthenticationException, IOException {
        if (StringUtils.isNotBlank(deviceType)) {
            login.setDeviceType(deviceType);
        } else {
            return ResultBean.getFailed().setM("设备信息错误");
        }
        if (StringUtils.isNotBlank(os)) {
            login.setOS(os);
        } else {
            return ResultBean.getFailed().setM("OS信息错误");
        }
        LoginUserBean old = this.iAuthenticationBizService.actFindByUsername(login.getUsername()).getD();
        if (old == null) {
            return ResultBean.getFailed().setM("账号不存在,请确认后重试！");
        }
        try {
            if (login != null && login.getUsername() != null && login.getPassword() != null) {
                final Authentication authentication = autshenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                login.getUsername(),
                                login.getPassword()
                        )
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                return ResultBean.getFailed().setM("密码为空，请重试！");
            }
        } catch (AuthenticationException ex) {
            return ResultBean.getFailed().setM("密码信息错误，请重试！");
        }
        //更新用户登录时间
        iAuthenticationBizService.actUpdateUserLoginTime(old.getId()).getD();

        // Reload password post-security so we can generate token
        final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(login.getUsername());
        String userId = jwtUser != null ? jwtUser.getId() : null;
        // 检查设备绑定信息，如果未绑定，添加绑定信息。
        TerminalBindBean bind = IDeviceBizService.actGetTerminalBind(login.getIdentify()).getD();
        Boolean bound = false;
        if (bind == null && StringUtils.isNotBlank(userId)) {
            TerminalDeviceBean deviceBean = new TerminalDeviceBean();
            deviceBean.setDeviceName(login.getDeviceName());
            deviceBean.setDeviceType(login.getDeviceType());
            deviceBean.setIdentify(login.getIdentify());
            deviceBean.setMac(login.getMac());
            deviceBean.setSerialNum(login.getSerialNum());
            deviceBean.setEmployeeId(userId);
            IDeviceBizService.actBindTerminalDevice(userId, deviceBean);
            bound = true;
        } else {
            if (bind.getLoginUserId().equals(userId)) {
                bound = true;
            } else {
                return ResultBean.getFailed().setM("该设备已经绑定其他用户！");
            }
        }
        if (bound) {
            //Redis保存Token
            UserBean returnUser = getUserBean(deviceType, jwtUser, userId);
            returnUser.setLastLoginTime(old.getLastLoginTime()); //最近登录时间

            UserProfileBean profile = iUserProfileBizService.getProfile(userId).getD();
            if (profile != null) {
                returnUser.setDisplayName(profile.getUsername()); //用户姓名
                returnUser.setAvatarFileId(profile.getAvatarFileId()); //头像
            }
            //返回token及用户信息
            return ResultBean.getSucceed().setD(returnUser);
        } else {
            return ResultBean.getFailed().setM("绑定失败, 请联系管理员!");
        }
    }


    /**
     * 获取短信验证码
     *
     * @param cell 手机号
     * @return
     */
    @RequestMapping(value = "/login/{cell}/verifycode", method = RequestMethod.GET)
    public ResultBean loginByVerifycode(@PathVariable("cell") String cell) {
        ResultBean result = iAuthenticationBizService.actGetCellCode(cell);
        if (result.isSucceed()) {
            String verifyCode = (String) result.getD();
            if (StringUtils.isNotBlank(verifyCode)) {
                String message = "验证码为:" + verifyCode + "，有效期为5分钟，请尽快输入。";
                List<String> cells = new ArrayList<>();
                cells.add(cell);
                ResultBean r = jMessageBizService.actSendSMS(cells, message);
                if(r.isSucceed()){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("verifycode", verifyCode);
                    map.put("cell", cell);
                    return ResultBean.getSucceed().setM("发送短信验证码成功").setD(map);
                }
            }
            return ResultBean.getFailed().setM("发送短信验证码失败，请联系管理员！");
        } else {
            return result;
        }
    }

    /**
     * 短信验证码登录
     *
     * @param cell  手机号
     * @param login PadLoginBean  登录用户对象 ，验证码放在 password中传输过来
     * @return ResultBean
     */
    @RequestMapping(value = "/login/{cell}", method = RequestMethod.POST)
    public ResultBean loginByVerifycode(@PathVariable("cell") String cell, @RequestBody PadLoginBean login, HttpServletRequest req, @RequestHeader("deviceType") String deviceType, @RequestHeader("OS") String os) throws AuthenticationException, IOException {
        String verifycode = login.getPassword();
        if (StringHelper.isBlock(verifycode)) {
            return ResultBean.getFailed().setM("验证码为空，请重试！");
        }
        if (StringUtils.isNotBlank(deviceType)) {
            login.setDeviceType(deviceType);
        } else {
            return ResultBean.getFailed().setM("设备信息错误");
        }
        if (StringUtils.isNotBlank(os)) {
            login.setOS(os);
        } else {
            return ResultBean.getFailed().setM("OS信息错误");
        }
        // 根据用户账号查询用户基本信息
        LoginUserBean oldLogin = iAuthenticationBizService.actFindByUsername(cell).getD();
        if (oldLogin == null || StringUtils.isBlank(oldLogin.getId()) || StringUtils.isBlank(oldLogin.getUsername()) || StringUtils.isBlank(oldLogin.getPassword())) {
            return ResultBean.getFailed().setM("账号不存在，请重试！");
        }
        //获取验证码
        String oldVerifyCode = iAuthenticationBizService.actFindCellCode(cell).getD();
        if (StringUtils.isBlank(oldVerifyCode) || !oldVerifyCode.equals(verifycode)) {
            return ResultBean.getFailed().setM("输入的短信验证码错误，请重试！");
        }
        try {
            final Authentication authentication = autshenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            oldLogin.getUsername(),
                            oldLogin.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            return ResultBean.getFailed().setM("密码错误，请重试！");
        }

        final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(cell);
        String userId = jwtUser.getId();
        // 检查设备绑定信息，如果未绑定，添加绑定信息。
        TerminalBindBean bind = IDeviceBizService.actGetTerminalBind(login.getIdentify()).getD();
        Boolean bound = false;
        if (bind == null) {
            TerminalDeviceBean deviceBean = new TerminalDeviceBean();
            deviceBean.setDeviceName(login.getDeviceName());
            deviceBean.setDeviceType(login.getDeviceType());
            deviceBean.setIdentify(login.getIdentify());
            deviceBean.setMac(login.getMac());
            deviceBean.setSerialNum(login.getSerialNum());
            IDeviceBizService.actBindTerminalDevice(userId, deviceBean);
            bound = true;
        } else {
            if (bind.getLoginUserId().equals(userId)) {
                bound = true;
            }
        }
        if (bound) {
            //获取客户端设备类型
            //Redis保存Token成功后返回
            UserBean returnUser = getUserBean(deviceType, jwtUser, userId);

/*            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", token);
            map.put("userID", jwtUser.getId());*/
            //返回token及用户信息
            return ResultBean.getSucceed().setD(returnUser);
        } else {
            return ResultBean.getFailed().setM("绑定失败");
        }
    }

    private UserBean getUserBean(String deviceType, JwtUser jwtUser, String userId) {
        String token = iLoginTokenBizService.refreshLoginTokenInfo(jwtUser.getUsername(), deviceType);

        UserBean returnUser = new UserBean();
        returnUser.setToken(token);
        returnUser.setUserID(userId);
        returnUser.setRoles(iAuthenticationBizService.actGetSysRoleCodes(userId).getD());
        return returnUser;
    }


    /**
     * 修改密码
     *
     * @param body cell verifycode password
     * @return ResultBean
     */
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public ResultBean changePassword(@RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        if (obj == null) {
            return ResultBean.getFailed().setM("参数错误，请重试！");
        }
        String cell = obj.getString("cell");
        if (StringUtils.isBlank(cell)) {
            return ResultBean.getFailed().setM("手机号码不得为空，请重试！");
        }
        LoginUserBean loginUserBean = iAuthenticationBizService.actFindByUsername(cell).getD();
        if (loginUserBean == null || StringUtils.isBlank(loginUserBean.getId()) || StringUtils.isBlank(loginUserBean.getUsername()) || StringUtils.isBlank(loginUserBean.getPassword())) {
            return ResultBean.getFailed().setM("输入的手机号码不存在，请重试！");
        }
        String verifycode = obj.getString("verifycode");
        if (StringUtils.isBlank(verifycode)) {
            return ResultBean.getFailed().setM("短信验证码不得为空，请重试！");
        }
        String password = obj.getString("password");
        if (StringUtils.isBlank(password)) {
            return ResultBean.getFailed().setM("密码不得为空，请重试！");
        }
        String oldVerifyCode = iAuthenticationBizService.actFindCellCode(cell).getD();
        if (StringUtils.isBlank(oldVerifyCode) || !oldVerifyCode.equals(verifycode)) {
            return ResultBean.getFailed().setM("短信验证码错误，请重新获取！");
        }
        loginUserBean.setPassword(password);
        return iAuthenticationBizService.actPadSavaLoginUser(loginUserBean);
    }


    /**
     * 验证 短信验证码 接口
     *
     * @param cell       手机号
     * @param verifycode 短信验证码
     * @return ResultBean
     */
    @RequestMapping(value = "/login/check/{cell}/{verifycode}", method = RequestMethod.GET)
    public ResultBean loginByVerifycode(@PathVariable("cell") String cell, @PathVariable("verifycode") String verifycode, HttpServletRequest req) {
        return iAuthenticationBizService.actCheckCellVerifyCode(cell, verifycode);
    }

    @RequestMapping(value = "/login/webtokenbypadtoken")
    public ResultBean<String> loginByPadToken(@RequestParam("padToken") String padToken)  {

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String userName = jwtTokenUtil.getUsernameFromToken(padToken);
        String clientType = "PC";
        String webToken = iLoginTokenBizService.getLoginTokenInfo(userName, clientType);
        if (webToken != null) {
            return ResultBean.getSucceed().setD(webToken);
        } else {
            try {
                webToken = iLoginTokenBizService.setLoginTokenInfo(userName, clientType);
            } catch (InterruptedException e) {

            }
            return ResultBean.getSucceed().setD(webToken);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResultBean loginOut(HttpServletRequest request, HttpServletResponse response) {
        try {

            String token = request.getHeader("userToken");
            iLoginTokenBizService.clearLoginToken(token);
            return ResultBean.getSucceed().setM("LOGOUT_SUCCESS");
        } catch (Exception e) {
            return ResultBean.getFailed().setM("LOGOUT_ERROR");
        }
    }


}
