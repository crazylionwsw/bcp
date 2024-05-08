package com.fuze.bcp.auth.business;

import com.fuze.bcp.api.auth.bean.LogManagementBean;
import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.bean.SysResourceBean;
import com.fuze.bcp.api.auth.bean.SysRoleBean;
import com.fuze.bcp.api.auth.jwt.JwtTokenUtil;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.auth.domain.LoginUser;
import com.fuze.bcp.auth.domain.SysResource;
import com.fuze.bcp.auth.domain.SysRole;
import com.fuze.bcp.auth.service.ILoginUserService;
import com.fuze.bcp.auth.service.ISysResourceService;
import com.fuze.bcp.auth.service.ISysRoleService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.dubbo.annotation.AMQPConfiguration;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.EncryUtil;
import com.fuze.bcp.utils.StringHelper;
import com.fuze.bcp.utils.VerifyCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static com.fuze.bcp.api.auth.service.ILoginTokenBizService.*;

/**
 * 认证服务实现类
 * Created by sean on 2017/5/19.
 */
@Service
public class BizAuthenticationService implements IAuthenticationBizService {

    private final Logger logger = LoggerFactory.getLogger(BizAuthenticationService.class);

    @Autowired
    ILoginUserService iLoginUserService;

    @Autowired
    ISysRoleService iSysRoleService;

    @Autowired
    ISysResourceService iSysResourceService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    public MongoTemplate mongoTemplate;

    public ResultBean<List<String>> actGetSpecialRoleByUserId(String userId) {
        List<String> specialRole = new ArrayList<>();
        // G_ASSISTANT_APPROVAL，G_FIRST_APPROVAL，G_SECOND_APPROVAL，G_CHIEF_APPROVAL
        specialRole.add("G_ASSISTANT_APPROVAL");
        specialRole.add("G_FIRST_APPROVAL");
        specialRole.add("G_SECOND_APPROVAL");
        specialRole.add("G_CHIEF_APPROVAL");
        LoginUser loginUser = iLoginUserService.getAvailableOne(userId);
        List<String> activitiUsers = loginUser.getActivitiUserRoles();
        specialRole.retainAll(activitiUsers);
        return ResultBean.getSucceed().setD(specialRole);
    }

    @Override
    public ResultBean<LoginUserBean> actFindByUsername(String username) {
        LoginUser user = iLoginUserService.getByUsername(username);
        if (user == null) {
            return ResultBean.getSucceed();
        }
        return ResultBean.getSucceed().setD(mappingService.map(user, LoginUserBean.class));
    }

    public ResultBean<LoginUserBean> actUpdateUserLoginTime(String id) {
        LoginUser user = iLoginUserService.getOne(id);
        user.setLastLoginTime(DateTimeUtils.getCreateTime());

        user = iLoginUserService.save(user);
        return ResultBean.getSucceed().setD(mappingService.map(user, LoginUserBean.class));
    }

    @Override
    public ResultBean<DataPageBean<LoginUserBean>> actSearchLoginUsers(Integer currentPage, LoginUserBean loginUserBean) {
        LoginUser loginuser = mappingService.map(loginUserBean, LoginUser.class);

        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").gt(DataStatus.TEMPSAVE));

        if (!StringUtils.isEmpty(loginuser.getUsername()))
            query.addCriteria(Criteria.where("username").regex(Pattern.compile("^.*"+ loginuser.getUsername() +".*$", Pattern.CASE_INSENSITIVE)));
        if (!StringUtils.isEmpty(loginuser.getUserRoleIds()) && loginuser.getUserRoleIds().size() > 0)
            query.addCriteria(Criteria.where("userRoleIds").in(loginuser.getUserRoleIds().get(0)));

        Pageable pageable = new PageRequest(currentPage, 20);
        query.with(LoginUser.getSortDESC("ts")).with(pageable);
        List list = mongoTemplate.find(query,LoginUser.class);
        Page<LoginUser> page  = new PageImpl(list,pageable, mongoTemplate.count(query,LoginUser.class));
        return ResultBean.getSucceed().setD(mappingService.map(page, LoginUserBean.class));
    }

    @Override
    public ResultBean<List<LoginUserBean>> actSearchByUsername(String username) {
        List<LoginUser> user = iLoginUserService.searchByUsername(username);
        return ResultBean.getSucceed().setD(mappingService.map(user, LoginUserBean.class));
    }

    @Override
    public ResultBean<String> actLogout(LoginUserBean user) {

        return null;
    }

    @Override
    public Boolean actCheckCellExist(String cell) {
        // 验证手机号码是否存在
/*        LoginUser loginUser = loginUserRepository.findByDataStatusAndUsername(DataStatus.SAVE,cell);
        if(loginUser == null){
            return false;
        }*/
        return true;
    }

    @Override
    public ResultBean<LoginUserBean> actUpdatePassword(LoginUserBean user) {
        try {
            LoginUser loginUser = iLoginUserService.getOne(user.getId());
            if (loginUser == null) {
                return ResultBean.getFailed();
            }
            loginUser.setPassword(EncryUtil.MD5(user.getPassword()));
            iLoginUserService.save(loginUser);
            return ResultBean.getSucceed();
        } catch (Exception e) {
            //logger.error("Error:修改密码失败",e);
        }
        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<String> actFindPassword(LoginUserBean user) {
        return null;
    }

    @Override
    public ResultBean<LoginUserBean> actFindLoginByToken(String token) {
        String username = new JwtTokenUtil().getUsernameFromToken(token);
        if (StringHelper.isBlock(username)) {
            return ResultBean.getFailed();
        }
        LoginUser loginUser = iLoginUserService.getByUsername(username);
        if (loginUser == null)
            return ResultBean.getFailed();
        else {
            return ResultBean.getSucceed().setD(mappingService.map(loginUser, LoginUserBean.class));
        }

    }

    @Override
    public ResultBean<String> actFindCellCode(String cell) {
        if (StringHelper.isBlock(cell)) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_LOGIN_CELL_NULL"));
        }
        Jedis jedis = jedisPool.getResource();
        try {
            String code = null;
            try {
                if (jedis.exists(LOGIN_VERIFYCODE_HASH + cell)) {
                    code = jedis.hget(LOGIN_VERIFYCODE_HASH + cell, VERIFYCODE_TYPE + cell);
                }
            } catch (Exception e) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_SYS_ERROR"));
            } finally {
                jedis.close();
            }
            if (StringHelper.isBlock(code))
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_LOGIN_GET_VERIFYCODE_ERROR"));
            else
                return ResultBean.getSucceed().setD(code);
        }finally {
            jedis.close();
        }

    }

    @Override
    public ResultBean<String> actGetCellCode(String cell) {
        Jedis jedis = jedisPool.getResource();
        String verifyCode = null;
        try {
            verifyCode = String.valueOf(VerifyCodeUtil.getSingleton().getSecurityCode());
            jedis.hset(LOGIN_VERIFYCODE_HASH + cell, VERIFYCODE_TYPE + cell, verifyCode);
            jedis.expire(LOGIN_VERIFYCODE_HASH + cell, VERIFYCODE_TIMEOUT);
            logger.debug("USER CELL IS: " + cell + ".VERIFYCODE is:" + verifyCode);
        } finally {
            jedis.close();
        }
        return ResultBean.getSucceed().setD(verifyCode);
    }

    @Override
    public ResultBean<List<SysResourceBean>> actGetUserSysResources(String userId) {
        List<SysResource> sysResources = new ArrayList<SysResource>();
        LoginUser user = iLoginUserService.getOne(userId);
        if (user != null) {
            List<String> sysResourceIds = new ArrayList<String>();

            if (user.getUserRoleIds().size() > 0) {
                //获取用户角色
                List<SysRole> sysRoles = iSysRoleService.getAvaliableList(user.getUserRoleIds());
                for (SysRole sysRole : sysRoles) {
                    sysResourceIds.addAll(sysRole.getSysResourceIds());
                }
                if (sysResourceIds.size() > 0)
                    sysResources = iSysResourceService.getAvaliableList(sysResourceIds);
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(sysResources, SysResourceBean.class));
    }

    @Override
    public ResultBean<LoginUserBean> actGetLoginUser(String userId) {
        LoginUser user = iLoginUserService.getOne(userId);
        if (user == null) {
            return ResultBean.getSucceed();
        } else {
            LoginUserBean loginUserBean = mappingService.map(user, LoginUserBean.class);
            return ResultBean.getSucceed().setD(loginUserBean);
        }
    }

    @Override
    public ResultBean<List<LoginUserBean>> actLookupLoginUser() {
        List<LoginUser> loginUsers = iLoginUserService.getAvaliableAll();
        List<LoginUserBean> loginUserBean = new ArrayList<>();
        for (LoginUser lo : loginUsers) {
            LoginUserBean louBean = new LoginUserBean();
            louBean.setId(lo.getId());
            louBean.setUsername(lo.getUsername());
            loginUserBean.add(louBean);
        }
        return ResultBean.getSucceed().setD(loginUserBean);
    }

    @Override
    @AMQPConfiguration(AMQPMsgCode = "LOG")
    public ResultBean<LoginUserBean> actSaveLoginUser(LoginUserBean userBean) {
        LoginUser user = mappingService.map(userBean, LoginUser.class);
        if (user.getPassword() != null && !("").equals(user.getPassword())) { // 123456 ， gdywgfdugugfsudfgsiudgfsidfgdbv
            //密码进行MD5加密
            user.setPassword(EncryUtil.MD5(user.getPassword()));
        } else {
            if (user.getId() != null) {
                LoginUser olduser = iLoginUserService.getOne(user.getId());
                if (olduser != null) {
                    user.setPassword(olduser.getPassword());
                }
            }
        }
        user = iLoginUserService.save(user);

        return ResultBean.getSucceed().setD(mappingService.map(user, LoginUserBean.class));
    }

    @Override
    public ResultBean<DataPageBean<LoginUserBean>> actGetLoginUsers(Integer currentPage) {
        Page<LoginUser> users = iLoginUserService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(users, LoginUserBean.class));
    }

    @Override
    public ResultBean<LoginUserBean> actDeleteLoginUser(String userId) {

        LoginUser user = iLoginUserService.getOne(userId);
        LoginUserBean loginUserBeen = new LoginUserBean();
        if (user != null) {
            user = iLoginUserService.delete(userId);
            loginUserBeen.setUsername(user.getUsername());
            loginUserBeen.setDeviceNum(user.getDeviceNum());
            loginUserBeen.setActivitiUserId(user.getActivitiUserId());
            loginUserBeen.setActivitiUserRoles(user.getActivitiUserRoles());
            loginUserBeen.setUserRoleIds(user.getUserRoleIds());
            loginUserBeen.setActivitiUserRoles(user.getActivitiUserRoles());
            return ResultBean.getSucceed().setD(mappingService.map(user, LoginUserBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<LoginUserBean>> actGetLoginUserByUserRoleIds(String userRoleId) {
        List<LoginUser> loginUsers = iLoginUserService.getLoginUserByUserRoleIds(userRoleId);
        List<LoginUserBean> loginUserBeens = new ArrayList<>();
        for (int i = 0; i < loginUsers.size(); i++) {
            LoginUserBean loginUserBean = mappingService.map(loginUsers.get(i), LoginUserBean.class);
            loginUserBeens.add(loginUserBean);
        }
        return ResultBean.getSucceed().setD(loginUserBeens);
    }

    @Override
    public ResultBean<List<LoginUserBean>> actGetLoginUserByGroupId(String groupId) {
        List<LoginUser> loginUsers = iLoginUserService.getLoginUserByGroupId(groupId);
        List<LoginUserBean> loginUserBeens = new ArrayList<>();
        for (int i = 0; i < loginUsers.size(); i++) {
            LoginUserBean loginUserBean = mappingService.map(loginUsers.get(i), LoginUserBean.class);
            loginUserBeens.add(loginUserBean);
        }
        return ResultBean.getSucceed().setD(loginUserBeens);
    }

    @Override
    public ResultBean<LoginUserBean> actGetLoginUserByActivitiUserId(String activitiUserId) {
        LoginUser loginUser = iLoginUserService.getLoginUserByActivitiUserId(activitiUserId);
        LoginUserBean loginUserBean = mappingService.map(loginUser, LoginUserBean.class);
        return ResultBean.getSucceed().setD(loginUserBean);
    }

    @Override
    public ResultBean<SysRoleBean> actGetSysRole(String roleId) {
        SysRole sysRole = iSysRoleService.getOne(roleId);
        if (sysRole != null) {
            return ResultBean.getSucceed().setD(mappingService.map(sysRole, SysRoleBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<SysRoleBean> actSaveSysRole(SysRoleBean roleBean) {
        SysRole savedRole = iSysRoleService.save(mappingService.map(roleBean, SysRole.class));
        return ResultBean.getSucceed().setD(mappingService.map(savedRole, SysRoleBean.class));
    }

    @Override
    public ResultBean<SysRoleBean> actDeleteSysRole(String roleId) {

        SysRole sysRole = iSysRoleService.getOne(roleId);

        if (sysRole != null) {
            sysRole = iSysRoleService.delete(roleId);
            return ResultBean.getSucceed().setD(mappingService.map(sysRole, SysRoleBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<SysRoleBean>> actGetSysRoles(Integer currentPage) {

        Page<SysRole> roles = iSysRoleService.getAll(currentPage);

        return ResultBean.getSucceed().setD(mappingService.map(roles, SysRoleBean.class));
    }

    @Override
    public ResultBean<List<SysRoleBean>> actGetSysRolesByIds(List<String> roleIds) {

        List roles = iSysRoleService.getAvaliableList(roleIds);

        return ResultBean.getSucceed().setD(mappingService.map(roles, SysRoleBean.class));

    }

    @Override
    public ResultBean<List<String>> actGetSysRoleCodes(String userId) {
        LoginUser user = iLoginUserService.getOne(userId);
        List<String> roleCodes = new ArrayList<String>();
        if (user != null) {
            List<String> roleIds = user.getUserRoleIds();
            if (roleIds != null) {
                List<SysRole> roles = iSysRoleService.getAvaliableList(roleIds);
                if (roles != null) {
                    for (SysRole role : roles) {
                        roleCodes.add(role.getCode());
                    }
                }
            }
        }

        return ResultBean.getSucceed().setD(roleCodes);
    }


    @Override
    public ResultBean<List<APILookupBean>> actLookupSysRoles() {

        List<SysRole> sysRoles = iSysRoleService.getAvaliableAll();

        return ResultBean.getSucceed().setD(mappingService.map(sysRoles, APILookupBean.class));
    }

    @Override
    public ResultBean<SysResourceBean> actGetSysResource(String resourceId) {

        SysResource sysResource = iSysResourceService.getOne(resourceId);

        return ResultBean.getSucceed().setD(mappingService.map(sysResource, SysResourceBean.class));
    }

    @Override
    public ResultBean<SysResourceBean> actSaveSysResource(SysResourceBean resourceBean) {

        SysResource sysResource = mappingService.map(resourceBean, SysResource.class);

        sysResource = iSysResourceService.save(sysResource);

        return ResultBean.getSucceed().setD(mappingService.map(sysResource, SysResourceBean.class));
    }

    @Override
    public ResultBean<SysResourceBean> actDeleteSysResource(String resourceId) {

        SysResource sysResource = iSysResourceService.getOne(resourceId);
        if (sysResource != null) {
            sysResource = iSysResourceService.delete(resourceId);
            return ResultBean.getSucceed().setD(mappingService.map(sysResource, SysResourceBean.class));
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<SysResourceBean>> actGetSysResources(Integer currentPage) {

        Page<SysResource> sysResources = iSysResourceService.getAll(currentPage);

        return ResultBean.getSucceed().setD(mappingService.map(sysResources, SysResourceBean.class));
    }

    @Override
    public ResultBean<List<SysResourceBean>> actGetChildSysResources(String parentId) {

        List<SysResource> sysResources = iSysResourceService.getChildrenOrderByCode(parentId);
        return ResultBean.getSucceed().setD(mappingService.map(sysResources, SysResourceBean.class));
    }

    @Override
    public ResultBean<List<SysResourceBean>> actGetSysResources(List<String> resourceIds) {
        if (resourceIds.size() > 0) {
            List<SysResource> sysResources = iSysResourceService.getAvaliableList(resourceIds);
            return ResultBean.getSucceed().setD(mappingService.map(sysResources, SysResourceBean.class));

        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<APITreeLookupBean>> actLookupSysResources() {
        List<SysResource> sysResources = iSysResourceService.getLookups(null, 0);
        return ResultBean.getSucceed().setD(mappingService.map(sysResources, APITreeLookupBean.class));
    }


    @Override
    public ResultBean<List<LoginUserBean>> actPadSavaLoginUser(LoginUserBean loginUserBean) {
        if (loginUserBean == null || StringHelper.isBlank(loginUserBean.getId()) || StringHelper.isBlank(loginUserBean.getPassword())) {
            ResultBean.getFailed().setM(messageService.getMessage("MSG_LOGIN_LOGINUSER_NULL"));
        }
        LoginUser loginUser = this.iLoginUserService.save(mappingService.map(loginUserBean, LoginUser.class));
        if (loginUser == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_LOGIN_CHANGEPASSWORD_ERROR"));
        } else {
            return ResultBean.getSucceed().setD(loginUser).setM(messageService.getMessage("MSG_LOGIN_CHANGEPASSWORD_SUCCESS"));
        }
    }

    @Override
    public ResultBean actDiscardLoginUser(String loginUserId) {
        LoginUser loginUser = iLoginUserService.discardLoginUser(loginUserId);
        return ResultBean.getSucceed().setD(mappingService.map(loginUser, LoginUserBean.class));
    }

    @Override
    public ResultBean<String> actCheckCellVerifyCode(String cell, String verifyCode) {
        if (StringHelper.isBlock(cell)) {
            return ResultBean.getSucceed().setM(messageService.getMessage("MSG_LOGIN_CELL_NULL"));
        }
        if (StringHelper.isBlock(verifyCode)) {
            return ResultBean.getSucceed().setM(messageService.getMessage("MSG_LOGIN_VERIFYCODE_NULL"));
        }
        Jedis jedis = jedisPool.getResource();
        String oldVerifyCode = null;
        try {
            if (jedis.exists(LOGIN_VERIFYCODE_HASH + cell)) {
                oldVerifyCode = jedis.hget(LOGIN_VERIFYCODE_HASH + cell, VERIFYCODE_TYPE + cell);
            }
            logger.debug("USER CELL IS: " + cell + ".VERIFYCODE is:" + verifyCode);
        } finally {
            jedis.close();
        }
        if (oldVerifyCode != null && oldVerifyCode.equals(verifyCode.trim())) {
            return ResultBean.getSucceed().setM(messageService.getMessage("MSG_LOGIN_CHECKED_SUCCESS"));
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_LOGIN_CHECKED_ERROR"));
        }
    }

    @Override
    public ResultBean<SysRoleBean> actGetAvailableSysRole(String roleId) {
        SysRole sysRole = iSysRoleService.getAvailableOne(roleId);
        if (sysRole != null) {
            return ResultBean.getSucceed().setD(mappingService.map(sysRole, SysRoleBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<LoginUserBean> actRenewLoginUser(LoginUserBean userBean) {
        LoginUser user = mappingService.map(userBean, LoginUser.class);
        if (user.getId() != null) {
            LoginUser olduser = iLoginUserService.getOne(user.getId());
            if (olduser != null) {
                user.setPassword(olduser.getPassword());
            }
        }
        LoginUser loginUser = iLoginUserService.save(user);
        return ResultBean.getSucceed().setD(mappingService.map(loginUser, LoginUserBean.class));
    }

    @Override
    public ResultBean<List<LogManagementBean>> actGetLogUpCount() {
        Jedis jedis = jedisPool.getResource();
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        List<LogManagementBean> logManagements = new ArrayList<LogManagementBean>();
        try{
            Set<String> keys = jedis.keys("L:U:*");
            Iterator<String> it=keys.iterator() ;
            while(it.hasNext()){
                String key = it.next();
                String keyInfo  = key.split(":")[2];
                Map<String, String> tokenMap = jedis.hgetAll(key);
                Object tokenValue = null;
                Object tokenType = null;
                Set  set = tokenMap.entrySet();
                Iterator   iterator=set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry  mapentry = (Map.Entry) iterator.next();
                    tokenValue = mapentry.getValue();
                    tokenType = mapentry.getKey();
                    Date createdDateFromToken = jwtTokenUtil.getCreatedDateFromToken(tokenValue.toString());
                    String time = this.getTime(createdDateFromToken.toString());
                    LogManagementBean logManagement = new LogManagementBean();
                    logManagement.setLogName(keyInfo);
                    LoginUser loginUser = iLoginUserService.getByUsername(keyInfo);
                    if(loginUser != null){
                        logManagement.setUserName(loginUser.getId());
                    }
                    String audienceFromToken = jwtTokenUtil.getAudienceFromToken(tokenValue.toString());
                    logManagement.setLogType(audienceFromToken);
                    logManagement.setLogTime(time);
                    logManagements.add(logManagement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return ResultBean.getSucceed().setD(logManagements);
    }

    @Override
    public ResultBean<LogManagementBean> actOffLine(String logName,String logType) {
        Jedis jedis = jedisPool.getResource();
        try {
            String key = LOGIN_USER_HASH+logName;
            Boolean exists = jedis.exists(key);
            if(exists){
                String keyType = TOKEN_TYPE+logType;
                jedis.hdel(key, keyType);
            }
            return ResultBean.getSucceed();
        }finally {
            jedis.close();
        }

    }

    private String getTime(String dt) throws Exception {
        if(dt != null){
            SimpleDateFormat sdf1= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf2.format(sdf1.parse(dt));
            return format;
        }
        return null;
    }

}
