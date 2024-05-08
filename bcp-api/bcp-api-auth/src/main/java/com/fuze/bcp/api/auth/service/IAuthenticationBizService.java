package com.fuze.bcp.api.auth.service;

import com.fuze.bcp.api.auth.bean.LogManagementBean;
import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.bean.SysResourceBean;
import com.fuze.bcp.api.auth.bean.SysRoleBean;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.APITreeLookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import sun.rmi.runtime.Log;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 认证服务接口类
 * Created by sean on 2017/5/19.
 */
public interface IAuthenticationBizService {


    /**
     * @param username
     * @return
     */
    ResultBean<LoginUserBean> actFindByUsername(@NotNull String username);

    ResultBean<LoginUserBean> actUpdateUserLoginTime(String id);

    /**
     * 模糊查询用户信息(带分页)
     * @param loginUserBean
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<LoginUserBean>> actSearchLoginUsers(Integer currentPage,LoginUserBean loginUserBean);

    /**
     * 根据用户名模糊查询用户信息
     * @param username
     * @return
     */
    ResultBean<List<LoginUserBean>> actSearchByUsername(String username);

    /**
     * 用户登出
     *
     * @param user
     * @return
     */
    ResultBean<String> actLogout(LoginUserBean user);

    /**
     * 验证手机号是否存在
     *
     * @param cell
     * @return
     */
    Boolean actCheckCellExist(@NotNull String cell);

    /**
     * 修改密码 pad
     *
     * @param user
     * @return
     */
    ResultBean<LoginUserBean> actUpdatePassword(LoginUserBean user);

    /**
     * 找回密码，通过手机短信找回密码
     *
     * @param user
     * @return
     */
    ResultBean<String> actFindPassword(LoginUserBean user);


    /**
     * 根据token获取用户的登录信息
     *
     * @param token
     * @return
     */
    ResultBean<LoginUserBean> actFindLoginByToken(@NotNull String token);

    /**
     * 根据手机号查找登陆验证码
     *
     * @param cell
     * @return
     */
    ResultBean<String> actFindCellCode(@NotNull String cell);

    /**
     * 验证 短信验证码 接口
     *
     * @param cell       手机号
     * @param verifyCode 短信验证码
     * @return
     */
    ResultBean<String> actCheckCellVerifyCode(@NotNull String cell,@NotNull String verifyCode);

    /**
     * 获取手机登录验证码
     *
     * @param cell
     * @return
     */
    ResultBean<String> actGetCellCode(@NotNull String cell);

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    ResultBean<List<SysResourceBean>> actGetUserSysResources(@NotNull String userId);

    /**
     * 获取单个用户数据
     *
     * @param userId
     * @return
     */
    ResultBean<LoginUserBean> actGetLoginUser(@NotNull String userId);

    /**
     * 获取所有用户(仅可用)
     * @return
     */
    ResultBean<List<LoginUserBean>> actLookupLoginUser();

    /**
     * 保存用户
     *
     * @param userBean
     * @return
     */
    ResultBean<LoginUserBean> actSaveLoginUser(LoginUserBean userBean);

    /**
     * 恢复用户
     * @param userBean
     * @return
     */
    ResultBean<LoginUserBean> actRenewLoginUser(LoginUserBean userBean);

    /**
     * 获取多个用户（有分页）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<LoginUserBean>> actGetLoginUsers(@Min(0) Integer currentPage);

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    ResultBean<LoginUserBean> actDeleteLoginUser(@NotNull String userId);

    ResultBean<List<LoginUserBean>> actGetLoginUserByGroupId(String groupId);

    ResultBean<List<LoginUserBean>> actGetLoginUserByUserRoleIds(String userRoleId);

    ResultBean<LoginUserBean> actGetLoginUserByActivitiUserId(String activitiUserId);
    /**
     * 获取单个系统角色数据
     *
     * @param roleId
     * @return
     */
    ResultBean<SysRoleBean> actGetSysRole(@NotNull String roleId);
    //获取单个系统角色数据(可用)
    ResultBean<SysRoleBean> actGetAvailableSysRole(String roleId);

    /**
     * 保存角色
     *
     * @param roleBean
     * @return
     */
    ResultBean<SysRoleBean> actSaveSysRole(SysRoleBean roleBean);

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    ResultBean<SysRoleBean> actDeleteSysRole(@NotNull String roleId);

    /**
     * 获取多个系统角色（有分页）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<SysRoleBean>> actGetSysRoles(@Min(0) Integer currentPage);


    /**
     * 根据ID列表获取
     *
     * @param roleIds
     * @return
     */
    ResultBean<List<SysRoleBean>> actGetSysRolesByIds(@NotNull List<String> roleIds);

    /**
     * 获取用户角色编码列表
     * @param userId
     * @return
     */
    ResultBean<List<String>> actGetSysRoleCodes(String userId);

    /**
     * 获取下拉列表
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupSysRoles();

    /**
     * 获取系统资源（单个）
     *
     * @param resourceId
     * @return
     */
    ResultBean<SysResourceBean> actGetSysResource(@NotNull String resourceId);

    /**
     * 保存系统资源
     *
     * @param resourceBean
     * @return
     */
    ResultBean<SysResourceBean> actSaveSysResource(SysResourceBean resourceBean);

    /**
     * 删除系统资源
     *
     * @param resourceId
     * @return
     */
    ResultBean<SysResourceBean> actDeleteSysResource(@NotNull String resourceId);

    /**
     * 获取多个系统资源（有分页）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<SysResourceBean>> actGetSysResources(@NotNull @Min(0) Integer currentPage);

    /**
     * 根据父ID取得下级资源
     *
     * @param parentId
     * @return
     */
    ResultBean<List<SysResourceBean>> actGetChildSysResources(@NotNull String parentId);

    /**
     * 根据ID列表获取
     *
     * @param resourceIds
     * @return
     */
    ResultBean<List<SysResourceBean>> actGetSysResources(@NotNull List<String> resourceIds);

    /**
     * 获取下拉列表
     *
     * @return
     */
    ResultBean<List<APITreeLookupBean>> actLookupSysResources();

    /**
     * PAD端保存loginUser
     * @return
     */
    ResultBean<List<LoginUserBean>> actPadSavaLoginUser(LoginUserBean loginUserBean);

    /**
     *
     * @return
     */
    ResultBean actDiscardLoginUser(String loginUserId);

    ResultBean<List<String>> actGetSpecialRoleByUserId(String userId);

    /**
     *统计在线人数
     */
    ResultBean<List<LogManagementBean>> actGetLogUpCount();

    /**
     *下线登陆账户
     */
    ResultBean<LogManagementBean> actOffLine(String key,String type);
}
