package com.fuze.bcp.pad.controller;


import com.fuze.bcp.api.auth.jwt.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2017/5/27.
 */
@RestController
@RequestMapping(value = "/json")
public class BaseController {

    /**
     *  获取当前操作人的ID
     * @return
     */
    public String getOperatorId() {

        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        return jwtUser.getId();
    }
}
