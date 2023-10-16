package com.example.mall.common.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
public class SwaggerProperties {

    // api文档生成基础路径
    private String apiBasePackage;

    // 是否要登录认证
    private boolean enableSecurity;

    // 文档标题
    private String title;

    // 文档描述
    private String description;

    // 文档版本
    private String version;
}
