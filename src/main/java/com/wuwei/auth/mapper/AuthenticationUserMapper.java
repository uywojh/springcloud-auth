package com.wuwei.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuwei.auth.common.SysUser;

public interface AuthenticationUserMapper extends BaseMapper<SysUser> {

    SysUser loadUserByUsername(String username);
}
