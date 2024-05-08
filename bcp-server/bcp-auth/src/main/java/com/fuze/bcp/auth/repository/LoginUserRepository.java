package com.fuze.bcp.auth.repository;

import com.fuze.bcp.auth.domain.LoginUser;
import com.fuze.bcp.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Gumenghao on 2016/10/19.
 */
public interface LoginUserRepository extends BaseRepository<LoginUser,String>{

    /**
     * 根据用户名获取登录用户
     * @param username
     * @return
     */
    LoginUser findByUsername(String username);

    /**
     * 删除多余用户
     * @param username
     * @return
     */
    int deleteByUsername(String username);

    /**
     * 根据用户名查找有效账号
     * @param username
     * @param ds
     * @return
     */
    LoginUser findByDataStatusAndUsername(Integer ds, String username);

    /**
     * 通过用户名查找用户(带分页)
     * @param ds
     * @param username
     * @param pageable
     * @return
     */
    Page<LoginUser> findByDataStatusAndUsername(Integer ds,String username,Pageable pageable);

    Page<LoginUser> findByDataStatusAndUsernameLike(Integer ds, String username, Pageable pageable);

    List<LoginUser> findByDataStatusAndUsernameLike(Integer ds,String username);

    /**
     * 获取登陆用户所拥有的权限
     * @return
     * @param id
     * @param ds
     */
    LoginUser findByDataStatusAndId(Integer ds, String id);

    /**
     * 通过用户名查找用户
     * @param ds
     * @param username
     * @return
     */
    List<LoginUser> findTop10ByDataStatusAndUsernameStartingWith(Integer ds, String username);

    List<LoginUser> findByActivitiUserRoles(String groupId);

    LoginUser findByActivitiUserId(String activitiUserId);

    List<LoginUser> findByUserRoleIds(String userRoleId);
}
