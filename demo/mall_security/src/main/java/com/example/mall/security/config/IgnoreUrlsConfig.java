package com.example.mall.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

// 这里的ConfigurationProperties需要被实例化操作才行，因此加@bean将其实例化
@ConfigurationProperties(prefix = "secure.ignored")
@Getter
@Setter
public class IgnoreUrlsConfig {
    private List<String> urls = new ArrayList<>();
}
