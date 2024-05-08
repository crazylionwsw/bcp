package com.fuze.bcp.web.controller;


import com.fuze.bcp.api.bd.bean.*;
import com.fuze.bcp.api.bd.service.*;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.fuze.bcp.api.auth.jwt.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
