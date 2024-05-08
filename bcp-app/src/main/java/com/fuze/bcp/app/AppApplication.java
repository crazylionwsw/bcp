package com.fuze.bcp.app;

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
public class AppApplication {


	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
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
			}).addPathPatterns("/json/app/**")
					.excludePathPatterns("/json/app/login")
					.excludePathPatterns("/json/app/wechatlogin")
					.excludePathPatterns("/json/app/logout")
					.excludePathPatterns("/json/app/file/upload")
					.excludePathPatterns("/json/app/file/download/*")
					.excludePathPatterns("/json/app/token/*");
		}
	}
}
