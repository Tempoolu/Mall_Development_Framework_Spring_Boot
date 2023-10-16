package com.example.mall.controller;

import com.example.mall.common.api.CommonPage;
import com.example.mall.dto.UmsAdminLoginParam;
import com.example.mall.dto.UmsAdminParam;
import com.example.mall.dto.UmsAdminPasswordParam;
import com.example.mall.model.UmsAdmin;
import com.example.mall.model.UmsRole;
import com.example.mall.service.UmsAdminService;
import com.example.mall.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class UmsAdminController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    UmsAdminService adminService;

    @ApiOperation(value = "登录后返回token")
    @PostMapping("/login")
    public CommonResult login(@Validated @RequestBody UmsAdminLoginParam loginParam) {
        String token = adminService.login(loginParam.getUsername(), loginParam.getPassword());
        if (token == null) {
            return CommonResult.fail("Username or password incorrect");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public CommonResult<UmsAdminParam> register(@Validated @RequestBody UmsAdminParam adminParam) {
        UmsAdmin admin = adminService.register(adminParam);
        if (admin == null) {
            return CommonResult.fail();
        }
        return CommonResult.success(adminParam);
    }

    @ApiOperation(value = "刷新token")
    @GetMapping("/refresh/token")
    public CommonResult refreshToken(HttpServletRequest request) {
        String token =  request.getHeader(tokenHeader);
        String newToken = adminService.refreshToken(token);
        if (newToken == null) {
            return CommonResult.fail("Token is invalid");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", newToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    // todo 比较复杂，涉及到role, resource, menu等等
    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/info")
    public CommonResult getAdminInfo(Principal principal) {
        return null;
    }

    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public CommonResult logout() {
        // 什么都不用做
        return CommonResult.success(null);
    }

    @ApiOperation(value = "获取指定用户信息")
    @GetMapping("/{id}")
    public CommonResult<UmsAdmin> getAdmin(@PathVariable Long id) {
        UmsAdmin admin = adminService.getAdmin(id);
        if (admin == null) {
            return CommonResult.fail();
        }
        return CommonResult.success(admin);
    }

    @ApiOperation("分页获取所有用户信息，可通过用户名和姓名获取")
    @GetMapping("/list/all")
    public CommonResult<CommonPage<UmsAdmin>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {

        List<UmsAdmin> adminList = adminService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(adminList));
    }

    @ApiOperation("删除指定用户信息")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = adminService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改指定用户信息")
    @PostMapping("/update/{id}")
    public CommonResult update(@Validated @RequestBody UmsAdminParam adminParam, @PathVariable Long id) {
        int count = adminService.update(adminParam, id);
        if (count > 0) {
            return CommonResult.success(1);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改指定用户密码")
    @PostMapping("/update/password")
    public CommonResult updatePassword(@Validated @RequestBody UmsAdminPasswordParam passwordParam) {
        int result = adminService.updatePassword(passwordParam);
        if (result == -1) {
            return CommonResult.fail("User not exists");
        }
        if (result == -2) {
            return CommonResult.fail("Old password is wrong");
        }
        if (result == -3) {
            return CommonResult.fail("New password is same as old password");
        }
        if (result > 0) {
            return CommonResult.success(null);
        }
        return CommonResult.fail();
    }

    @ApiOperation("获取指定用户角色")
    @GetMapping("/roles/{id}")
    public CommonResult<List<UmsRole>> getRoles(@PathVariable Long id) {
        List<UmsRole> roleList = adminService.getRoles(id);
        return CommonResult.success(roleList);
    }

    @ApiOperation("为用户分配角色")
    @PostMapping("update/roles/{id}")
    public CommonResult updateRoles(@PathVariable Long id, @RequestBody List<Long> roleIdList) {
        int count = adminService.updateRoles(id, roleIdList);
        return CommonResult.success(roleIdList.size());
    }
}
