package com.fuze.bcp.pad.security;

import com.fuze.bcp.api.auth.jwt.JwtTokenUtil;
import com.fuze.bcp.api.auth.service.ILoginTokenBizService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    ILoginTokenBizService iLoginTokenBizService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

        String authToken = request.getHeader("userToken");
        String clientType = request.getHeader("deviceType");
        String OS = request.getHeader("OS");
        System.out.println("==============================userToken is=" + authToken);
         if (authToken != null) {
            //判断token是否存在
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //根据用户名，查找该用户保存的token信息
                String existToken = iLoginTokenBizService.getLoginTokenInfo(username, clientType);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                //判断是否和前台一致
                if (authToken.equals(existToken) && userDetails != null) {
                    // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                    // the database compellingly. Again it's up to you ;)
                    if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                    response.setHeader("Login-State", "status-success");
                } else {
                    //不一致为异地登录
                    response.setHeader("Login-State", "status-error");
                }
            } else {
                //异地登录或者token已过期
                response.setHeader("Login-State", "status-timeout");
            }
        } else {
            response.setHeader("Login-State", "status-success");
        }
        chain.doFilter(request, response);
    }

}