package com.fuze.bcp.api.auth.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by stephan on 20.03.16.
 */
public class JwtUser implements UserDetails {

    private final String id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    //最后一次密码重置的时间
    private final Date lastPasswordResetTime;
    //是否为系统用户
    private final Boolean isSystem;
    //工作流角色
    private final List<String> activitiUserRoles;

    public JwtUser(
          String id,
          String username,
          String password, Collection<? extends GrantedAuthority> authorities,
          Date lastPasswordResetTime,
          Boolean isSystem,
          List<String> activitiUserRoles
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.lastPasswordResetTime = lastPasswordResetTime;
        this.isSystem = isSystem;
        this.activitiUserRoles = activitiUserRoles;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public Date getLastPasswordResetTime() {
        return lastPasswordResetTime;
    }

    /**
     * 是否系统用户
     * @return
     */
    public Boolean getIsSystem() {return isSystem;};

    /**
     * 工作流角色
     * @return
     */
    public List<String> getActivitiUserRoles() {return activitiUserRoles;};
}
