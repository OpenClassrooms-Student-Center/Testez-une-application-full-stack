package com.openclassrooms.starterjwt.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class securityTestConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity htttp) throws Exception{
        htttp
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/register").permitAll()
                .anyRequest().authenticated();
    }
}
