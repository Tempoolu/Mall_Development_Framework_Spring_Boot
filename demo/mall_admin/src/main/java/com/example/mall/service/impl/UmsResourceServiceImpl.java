package com.example.mall.service.impl;

import com.example.mall.mapper.UmsResourceMapper;
import com.example.mall.model.UmsResource;
import com.example.mall.model.UmsResourceExample;
import com.example.mall.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsResourceServiceImpl implements UmsResourceService {

    @Autowired
    private UmsResourceMapper resourceMapper;

    @Override
    public List<UmsResource> listAll() {
        List<UmsResource> resourceList = resourceMapper.selectByExample(new UmsResourceExample());
        return resourceList;
    }
}
