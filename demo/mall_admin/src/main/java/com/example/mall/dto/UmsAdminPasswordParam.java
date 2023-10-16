package com.example.mall.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode
public class UmsAdminPasswordParam {
    @NotEmpty
    private String username;

    @NotEmpty
    private String oldPassword;

    @NotEmpty
    private String newPassword;
}
