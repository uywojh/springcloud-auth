package com.wuwei.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wuwei.auth.common.SecurityConstants;
import com.wuwei.auth.common.SysUser;
import com.wuwei.auth.common.SysUserRole;
import com.wuwei.auth.entity.SysPermission;
import com.wuwei.auth.mapper.SysPermissionMapper;
import com.wuwei.auth.mapper.SysUserRoleMapper;
import com.wuwei.auth.service.AuthenticationUserService;
import com.wuwei.auth.service.CustomUserDetailsService;
import com.wuwei.auth.utils.UserDetailsUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * oauth 验证用户
 */
@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final AuthenticationUserService customUserService;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysPermissionMapper sysPermissionMapper;

    /**
     *  通过userName去调用service 验证用户
     * @param username userName
     * @return 验证成功的用户details
     * @throws UsernameNotFoundException
     */
    @Override
    @Cacheable(value = SecurityConstants.CACHE_USER_DETAILS, key = "#user_details", unless = "#result == null")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = customUserService.loadUserByUsername(username);
        //查询用户有哪些角色
        List<Long> roleIds = sysUserRoleMapper
                .selectList(Wrappers.<SysUserRole>query().lambda().eq(SysUserRole::getUserId,sysUser.getUserId()))
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        if(CollUtil.isNotEmpty(roleIds)){
            //根据角色查询权限
            List<String> roleCodes = sysPermissionMapper.getPermission(roleIds)
                    .stream()
                    .map(SysPermission::getPermission)
                    .collect(Collectors.toList());
            sysUser.setRoleCode(roleCodes);
        }
        UserDetails userDetails = UserDetailsUtils.getUserDetails(sysUser);
        return userDetails;
    }




}
