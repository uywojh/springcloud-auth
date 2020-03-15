package com.wuwei.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuwei.auth.common.SysUser;
import com.wuwei.auth.mapper.AuthenticationUserMapper;
import com.wuwei.auth.service.AuthenticationUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Description //TODO 用户service$
 * @Date 21:08
 * @Author yzcheng90@qq.com
 **/
@Service
@AllArgsConstructor
public class AuthenticationUserServiceImpl extends ServiceImpl<AuthenticationUserMapper, SysUser> implements AuthenticationUserService {

    @Override
    public SysUser loadUserByUsername(String username) {
        return baseMapper.loadUserByUsername(username);
    }
}
