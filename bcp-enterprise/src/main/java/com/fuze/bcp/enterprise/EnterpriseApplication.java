package com.fuze.bcp.enterprise;

import com.alibaba.dubbo.rpc.RpcContext;
import com.fuze.bcp.api.auth.jwt.JwtUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
@EnableCaching
@ImportResource(locations = {"dubbo-consumer.xml"})
public class EnterpriseApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseApplication.class, args);
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
                    .excludePathPatterns("/json/login/**")
                    .excludePathPatterns("/json/file/upload")
                    .excludePathPatterns("/json/file/download/*")
                    .excludePathPatterns("/json/domesticoutfits")
                    .excludePathPatterns("/json/domesticoutfit")
                    .excludePathPatterns("/json/domesticoutfit/*")
                    .excludePathPatterns("/json//employee/businessmanager")
                    .excludePathPatterns("/json/employee/*")
                    .excludePathPatterns("/json/channels")
                    .excludePathPatterns("/json/cashsources");

        }
    }
}
