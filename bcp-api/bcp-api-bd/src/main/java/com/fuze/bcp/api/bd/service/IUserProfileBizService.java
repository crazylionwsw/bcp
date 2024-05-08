package com.fuze.bcp.api.bd.service;

import com.fuze.bcp.api.bd.bean.UserProfileBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by admin on 2017/5/31.
 */
public interface IUserProfileBizService {
    /**
     *
     * @param userId
     * @return
     */
    ResultBean<UserProfileBean> getProfile(@NotNull String userId);

    ResultBean<UserProfileBean> updateProfile(UserProfileBean userProfileBean);

    ResultBean<Map<String,Object>> actGetUserApproval(String userId);
}
