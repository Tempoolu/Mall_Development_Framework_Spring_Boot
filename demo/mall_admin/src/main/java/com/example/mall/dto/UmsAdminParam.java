package com.example.mall.dto;

import com.example.mall.model.UmsAdmin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.util.annotation.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@EqualsAndHashCode
public class UmsAdminParam {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @Email
    private String email;

    private String nickName;

    private String icon;

    private String note;
}
