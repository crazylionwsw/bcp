package com.fuze.bcp.app.security;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.jwt.JwtUserFactory;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by stephan on 20.03.16.
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IAuthenticationBizService iAuthenticationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserBean userBean = iAuthenticationService.actFindByUsername(username).getD();

        //获取角色列表
        if (userBean.getUserRoleIds().size() > 0) {
            userBean.setSysRoleList(iAuthenticationService.actGetSysRolesByIds(userBean.getUserRoleIds()).getD());
        }

        if (userBean == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
/*            return new User(userBean.getUsername(), userBean.getPassword(), true, true, true, true,
                    AuthorityUtils.createAuthorityList("USER"));*/
            return JwtUserFactory.create(userBean);
        }
    }
}