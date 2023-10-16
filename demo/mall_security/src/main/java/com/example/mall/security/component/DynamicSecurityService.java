package com.example.mall.security.component;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

public interface DynamicSecurityService {
    // Map<路径, 路径所需权限>
    Map<String, ConfigAttribute> loadDataSource();
}
