package com.example.mall.dao;

import com.example.mall.model.UmsAdminRoleRelation;
import com.example.mall.model.UmsResource;
import com.example.mall.model.UmsRole;

import java.util.List;

public interface UmsAdminRoleRelationDao {

    // 获得admin所有的resource
    List<UmsResource> getResourceList(Long id);

    // 获得admin所有的role
    List<UmsRole> getRoleList(Long id);

    // 批量添加admin的roles
    void insertAdminRoleList(List<UmsAdminRoleRelation> adminRoleRelation);

}
