package com.fuze.bcp.api.auth.jwt;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.bean.SysRoleBean;
import com.fuze.bcp.utils.DateTimeUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(LoginUserBean user) {

        Date LastPasswordResetTime = null;
        if (user.getLastPasswordResetTime() != null) {
            LastPasswordResetTime = DateTimeUtils.str2Date(user.getLastPasswordResetTime(), "yyyy-MM-dd");
        }
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getSysRoleList()),
                LastPasswordResetTime,
                user.getSystem(),
                user.getActivitiUserRoles()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<SysRoleBean> authorities) {
        if (authorities.size() > 0) {
            return authorities.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<GrantedAuthority>();
        }
    }
}
