package com.example.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.example.mall.bo.AdminUserDetails;
import com.example.mall.dao.UmsAdminRoleRelationDao;
import com.example.mall.dto.UmsAdminParam;
import com.example.mall.dto.UmsAdminPasswordParam;
import com.example.mall.mapper.UmsAdminRoleRelationMapper;
import com.example.mall.model.*;
import com.example.mall.service.UmsAdminCacheService;
import com.example.mall.service.UmsAdminService;
import com.example.mall.common.exception.Asserts;
import com.example.mall.common.util.RequestUtil;
import com.example.mall.mapper.UmsAdminLoginLogMapper;
import com.example.mall.mapper.UmsAdminMapper;
import com.example.mall.security.util.JwtTokenUtil;
import com.github.pagehelper.PageHelper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);

    @Autowired
    private UmsAdminMapper adminMapper;
    @Autowired
    private UmsAdminLoginLogMapper loginLogMapper;
    @Autowired
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;
    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails =  loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                Asserts.fail("Username or password incorrect");
            }
            if (!userDetails.isEnabled()) {
                Asserts.fail("User is not enabled");
            }
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("Login error: {}", e.getMessage());
        }
        return token;
    }

    @Override
    public UmsAdmin register(UmsAdminParam adminParam) {
        UmsAdminExample adminExample = new UmsAdminExample();
        adminExample.createCriteria().andUsernameEqualTo(adminParam.getUsername());
        List<UmsAdmin> adminList = adminMapper.selectByExample(adminExample);
        UmsAdmin admin = new UmsAdmin();

        // 检查重名
        if (adminList.size() == 0) {
            admin.setUsername(adminParam.getUsername());
            // 加密密码
            admin.setPassword(passwordEncoder.encode(adminParam.getPassword()));
            admin.setEmail(adminParam.getEmail());
            admin.setNickName(adminParam.getNickName());
            admin.setNote(adminParam.getNote());
            admin.setCreateTime(new Date());
            admin.setStatus(1);
            int count = adminMapper.insert(admin);
            if (count == 1) {
                return admin;
            }
        }
        return null;
    }

    @Override
    public String refreshToken(String token) {
        String newToken =  jwtTokenUtil.refreshHeadToken(token);
        return newToken;
    }

    @Override
    public UmsAdmin getAdmin(Long id) {
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andIdEqualTo(id);
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList != null && adminList.size() >= 1) {
            return adminList.get(0);
        }
        return null;
    }

    public List<UmsAdmin> list(String keyword, Integer pageSize, Integer PageNum) {
        PageHelper.startPage(PageNum, pageSize);
        UmsAdminExample example = new UmsAdminExample();
        if (!StrUtil.isEmpty(keyword)) {
            example.or().
                    andUsernameLike("%" + keyword + "%").
                    andNickNameLike("%" + keyword + "%");
        }
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        return adminList;
    }

    @Override
    public int delete(Long id) {
        getCacheService().deleteAdmin(id);
        int count = adminMapper.deleteByPrimaryKey(id);
        getCacheService().deleteResourceList(id);
        return count;
    }

    @Override
    public int update(UmsAdminParam adminParam, Long id) {
        UmsAdmin admin = adminMapper.selectByPrimaryKey(id);
        if (admin == null) {
            return 0;
        }
        if (!passwordEncoder.matches(adminParam.getPassword(), admin.getPassword())) {
            String newEncodePassword =  passwordEncoder.encode(adminParam.getPassword());
            adminParam.setPassword(newEncodePassword);
        } else {
            adminParam.setPassword(null);
        }
        BeanUtil.copyProperties(adminParam, admin, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));

        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andIdEqualTo(id);
        int count = adminMapper.updateByExampleSelective(admin, example);
        return count;
    }

    @Override
    public int updatePassword(UmsAdminPasswordParam passwordParam) {
        UmsAdmin admin = getAdminByUsername(passwordParam.getUsername());
        if (admin == null) {
            System.out.println(-1);
            return -1;  // -1找不到用户
        }
        if (!passwordEncoder.matches(passwordParam.getOldPassword(), admin.getPassword())) {
            System.out.println(-2);
            return -2;  // -2旧密码不正确
        }
        if (passwordEncoder.matches(passwordParam.getNewPassword(), admin.getPassword())) {
            System.out.println(-3);
            return -3;  // -3旧密码和新密码相同
        }
        String newEncodedPassword = passwordEncoder.encode(passwordParam.getNewPassword());
        admin.setPassword(newEncodedPassword);

        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(passwordParam.getUsername());
        int count = adminMapper.updateByExampleSelective(admin, example);

        getCacheService().deleteAdmin(admin.getId());
        return count;
    }

    public List<UmsRole> getRoles(Long id) {
        return adminRoleRelationDao.getRoleList(id);
    }

    public int updateRoles(Long id, List<Long> roleIdList) {
        List<UmsAdminRoleRelation> adminRoleRelationList = new ArrayList<>();
        UmsAdminRoleRelation relation = new UmsAdminRoleRelation();
        relation.setAdminId(id);

        for (Long roleId: roleIdList) {
            relation.setRoleId(roleId);
            adminRoleRelationList.add(relation);

        }

        // 删除admin的所有role
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        example.createCriteria().andAdminIdIn(roleIdList);
        adminRoleRelationMapper.deleteByExample(example);


        // 添加admin的所有role
        adminRoleRelationDao.insertAdminRoleList(adminRoleRelationList);
        System.out.println("adminRoleRelationList" + adminRoleRelationList);

        getCacheService().deleteResourceList(id);
        return roleIdList.size();
    }


    @Override
    public UmsAdmin getAdminByUsername(String username) {
        // 先从缓存中获取数据
        UmsAdmin admin = getCacheService().getAdmin(username);
        if (admin != null) {
            return admin;
        }
        // 如果缓存没有从数据库中获取
        UmsAdminExample adminExample = new UmsAdminExample();
        adminExample.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = adminMapper.selectByExample(adminExample);
        if (adminList != null && adminList.size()>0) {
            admin = adminList.get(0);
            // 将数据库中的数据存入缓存中
            getCacheService().setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        List<UmsResource> resourceList = getCacheService().getResourceList(adminId);
        // CollUtil.isEmpty是==null或isEmpty
        if (CollUtil.isNotEmpty(resourceList)) {
            return resourceList;
        }
        resourceList = adminRoleRelationDao.getResourceList(adminId);
        if (CollUtil.isNotEmpty(resourceList)) {
            getCacheService().setResourceList(adminId, resourceList);
        }
        return resourceList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        UmsAdmin admin = getAdminByUsername(username);   // admin进行一次缓存
        if (admin != null) {
            List<UmsResource> resourceList = getResourceList(admin.getId());  // resourceList进行一次缓存
            UserDetails userDetails = new AdminUserDetails(admin, resourceList);
            return userDetails;
        }
        throw new UsernameNotFoundException("Username or password incorrect.");
    }

    private void insertLoginLog(String username) {
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        UmsAdmin admin = getAdminByUsername(username);
        if (admin == null) return;
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(RequestUtil.getRequestIp(request));
        loginLogMapper.insert(loginLog);
    }

    @Override
    public UmsAdminCacheService getCacheService() {
        return SpringUtil.getBean(UmsAdminCacheService.class);
    }
}
