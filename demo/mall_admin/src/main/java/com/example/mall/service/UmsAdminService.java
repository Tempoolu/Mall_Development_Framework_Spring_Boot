package com.example.mall.service;

import com.example.mall.common.api.CommonPage;
import com.example.mall.dto.UmsAdminParam;
import com.example.mall.dto.UmsAdminPasswordParam;
import com.example.mall.model.UmsAdmin;
import com.example.mall.model.UmsResource;
import com.example.mall.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UmsAdminService {

    String login(String username, String password);
    UmsAdmin register(UmsAdminParam adminParam);
    String refreshToken(String token);
    UmsAdmin getAdmin(Long id);
    List<UmsAdmin> list(String keyword, Integer pageSize, Integer PageNum);
    int delete(Long id);
    int update(UmsAdminParam adminParam, Long id);
    int updatePassword(UmsAdminPasswordParam passwordParam);
    List<UmsRole> getRoles(Long id);
    int updateRoles(Long adminId, List<Long> roleIdList);
    UmsAdmin getAdminByUsername(String username);
    List<UmsResource> getResourceList(Long userId);
    UserDetails loadUserByUsername(String username);
    UmsAdminCacheService getCacheService();
}
