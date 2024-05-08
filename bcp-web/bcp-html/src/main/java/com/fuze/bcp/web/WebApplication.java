package com.fuze.bcp.web;

import com.alibaba.dubbo.rpc.RpcContext;
import com.fuze.bcp.api.auth.jwt.JwtUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2017/5/27.
 */

@SpringBootApplication
@EnableCaching
@ImportResource(locations = {"dubbo-consumer.xml"})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Configuration
    static class WebMvcConfigurer extends WebMvcConfigurerAdapter {

        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new HandlerInterceptorAdapter() {

                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                        throws Exception {

                    JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    RpcContext.getContext().setAttachment("OperatorId", jwtUser.getId());
                    return true;
                }
            }).addPathPatterns("/json/**")
                    .excludePathPatterns("/json/login")
                    .excludePathPatterns("/json/file/download/*")
                    .excludePathPatterns("/json/logout")
                    .excludePathPatterns("/json/token/*")
                    .excludePathPatterns("/json/charts/**")
                    .excludePathPatterns("/json/yesorno/**")
                    .excludePathPatterns("/json/customertransaction/*/survey")
                    .excludePathPatterns("/json/customertransaction/*/survey/**")
                    .excludePathPatterns("/json/dealersharings/export/**")
                    .excludePathPatterns("/json/businessbook/export/**")
                    .excludePathPatterns("/json/appointpayments/export/**")
                    .excludePathPatterns("/json/compensatorytransactions/export/**")
                    .excludePathPatterns("/json/businesstransactions/export/**")
                    .excludePathPatterns("/json/groupsharings/export/**")
                    .excludePathPatterns("/json/balanceaccount/export/**")

                    .excludePathPatterns("/json/oauth2")
                    .excludePathPatterns("/json/oauth2url/**")

                    .excludePathPatterns("/pdf/**")
                    .excludePathPatterns("/json/websocket/**")
                    .excludePathPatterns("/json/chargefeeplan/export/**");
//                    .excludePathPatterns("/json/orginfo/*/charts")
//                    .excludePathPatterns("/json/manager/*/charts")
//                    .excludePathPatterns("/json/orginfo/*/cardealer/count")
//                    .excludePathPatterns("/json/orginfos/*/loginuser")
//                    .excludePathPatterns("/json/employee/loginuser/**")
//                    .excludePathPatterns("/json/org/employee/**");
        }
    }

    @Configuration
    static class WebSocketConfig {
        @Bean
        public ServerEndpointExporter serverEndpointExporter() {
            return new ServerEndpointExporter();
        }
    }

}
