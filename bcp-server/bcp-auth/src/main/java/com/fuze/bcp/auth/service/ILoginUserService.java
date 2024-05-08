package com.fuze.bcp.auth.service;

import com.fuze.bcp.auth.domain.LoginUser;
import com.fuze.bcp.service.IBaseService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 系统用户维护服务
 * Created by admin on 2017/5/27.
 */
public interface ILoginUserService extends IBaseService<LoginUser> {

    LoginUser getByUsername(String username);

    Page<LoginUser> searchByUsername(String username,Integer currentPage);

    List<LoginUser> getLoginUserByGroupId(String groupId);

    LoginUser getLoginUserByActivitiUserId(String ActivitiUserId);

    List<LoginUser> searchByUsername(String username);

    LoginUser discardLoginUser(String loginUserId);

    List<LoginUser> getLoginUserByUserRoleIds(String userRoleId);

}
