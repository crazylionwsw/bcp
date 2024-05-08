package com.fuze.bcp.web.config;

import com.fuze.bcp.api.auth.jwt.JwtMD5PasswordEncoder;
import com.fuze.bcp.web.security.JwtAuthenticationEntryPoint;
import com.fuze.bcp.web.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new JwtMD5PasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // allow anonymous resource requests
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/fonts/**",
                        "/img/**",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers("/websocket/*").permitAll()
                .antMatchers("/json/login").permitAll()
                .antMatchers("/json/logout").permitAll()
                .antMatchers("/json/token/*").permitAll()
                .antMatchers("/json/customertransaction/*/survey").permitAll()
                .antMatchers("/json/customertransaction/*/survey/**").permitAll()
                .antMatchers("/json/file/download/*").permitAll()
                .antMatchers("/json/dealersharings/export/**").permitAll()
                .antMatchers("/json/businessbook/export/**").permitAll()
                .antMatchers("/json/appointpayments/export/**").permitAll()
                .antMatchers("/json/compensatorytransactions/export/**").permitAll()
                .antMatchers("/json/businesstransactions/export/**").permitAll()
                .antMatchers("/json/chargefeeplan/export/**").permitAll()
                .antMatchers("/json/balanceaccount/export/**").permitAll()
                .antMatchers("/json/groupsharings/export/**").permitAll()
                .antMatchers("/pdf/**").permitAll()
                .antMatchers("/json/websocket/**").permitAll()
                .antMatchers("/json/oauth2").permitAll()
                .antMatchers("/json/oauth2url/**").permitAll()

                //TODO:修改
//                .antMatchers("/json/orginfo/*/charts").permitAll()
//                .antMatchers("/json/manager/*/charts").permitAll()
//                .antMatchers("/json/orginfo/*/cardealer/count").permitAll()
//                .antMatchers("/json/orginfos/*/loginuser").permitAll()
//                .antMatchers("/json/employee/loginuser/**").permitAll()
//                .antMatchers("/json/org/employee/**").permitAll()
                .anyRequest().authenticated();


        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}

