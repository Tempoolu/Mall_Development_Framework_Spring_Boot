package com.example.mall.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.util.annotation.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
public class UmsAdminLoginParam {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
