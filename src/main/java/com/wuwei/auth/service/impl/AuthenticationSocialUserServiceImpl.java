package com.wuwei.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuwei.auth.common.SysSocialDetails;
import com.wuwei.auth.handler.LoginHandler;
import com.wuwei.auth.mapper.AuthenticationSocialUserMapper;
import com.wuwei.auth.service.AuthenticationSocialUserService;
import com.wuwei.auth.utils.UserDetailsUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description //TODO 第三方用户信息serivce$
 * @Date 21:07
 * @Author yzcheng90@qq.com
 **/
@Service
@AllArgsConstructor
public class AuthenticationSocialUserServiceImpl extends ServiceImpl<AuthenticationSocialUserMapper, SysSocialDetails> implements AuthenticationSocialUserService {

    private final Map<String, LoginHandler> loginHandlerMap;

    @Override
    public UserDetails getSocialUserInfo(String key) {
        String[] index = key.split("@");
        String type = index[0];
        String loginStr = index[1];
        return UserDetailsUtils.getUserDetails(loginHandlerMap.get(type).login(loginStr).getUser());
    }
}
