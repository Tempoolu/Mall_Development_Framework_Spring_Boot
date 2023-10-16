package com.example.mall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class MallSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return null;
    }
}
