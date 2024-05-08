package com.fuze.bcp.auth.service.impl;

import com.fuze.bcp.auth.domain.LoginUser;
import com.fuze.bcp.auth.repository.LoginUserRepository;
import com.fuze.bcp.auth.service.ILoginUserService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2017/5/27.
 */
@Service
public class LoginUserServiceImpl extends BaseServiceImpl<LoginUser, LoginUserRepository> implements ILoginUserService{

    @Autowired
    private LoginUserRepository loginUserRepository;

    @Override
    public LoginUser getByUsername(String username) {
        return loginUserRepository.findByDataStatusAndUsername(DataStatus.SAVE, username);
    }

    @Override
    public Page<LoginUser> searchByUsername(String username, Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage,20);
        return loginUserRepository.findByDataStatusAndUsernameLike(DataStatus.SAVE,username,pr);
    }

    @Override
    public List<LoginUser> getLoginUserByGroupId(String groupId) {
        return loginUserRepository.findByActivitiUserRoles(groupId);
    }

    @Override
    public LoginUser getLoginUserByActivitiUserId(String ActivitiUserId) {
        return loginUserRepository.findByActivitiUserId(ActivitiUserId);
    }

    @Override
    public List<LoginUser> searchByUsername(String username) {
        return loginUserRepository.findByDataStatusAndUsernameLike(DataStatus.SAVE,username);
    }

    @Override
    public LoginUser discardLoginUser(String loginUserId) {
        LoginUser loginUser = loginUserRepository.findOne(loginUserId);
        loginUser.setDataStatus(DataStatus.DISCARD);
        return loginUserRepository.save(loginUser);
    }

    @Override
    public List<LoginUser> getLoginUserByUserRoleIds(String userRoleId) {
        return loginUserRepository.findByUserRoleIds(userRoleId);
    }

}
