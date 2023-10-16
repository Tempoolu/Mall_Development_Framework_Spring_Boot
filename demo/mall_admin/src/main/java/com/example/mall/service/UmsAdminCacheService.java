package com.example.mall.service;

import com.example.mall.model.UmsAdmin;
import com.example.mall.model.UmsResource;

import java.util.List;

public interface UmsAdminCacheService {
    // 储存了admin信息 + admin-resource信息
    UmsAdmin getAdmin(String username);

    void setAdmin(UmsAdmin admin);

    void deleteAdmin(Long adminId);

    List<UmsResource> getResourceList(Long adminId);

    void setResourceList(Long adminId, List<UmsResource> resourceList);

    void deleteResourceList(Long adminId);

    UmsAdminCacheService getCacheService();
}
