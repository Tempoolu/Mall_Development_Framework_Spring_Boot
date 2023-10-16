package com.example.mall.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class WebLog {
    private String description;
    private String username;
    private Long startTime;
    private Integer spendTime;
    // 根路径
    private String basePath;
    private String uri;
    private String url;
    // 请求类型
    private String method;
    private String ip;
    // 请求参数
    private Object parameter;
    // 返回结果
    private Object result;
}
