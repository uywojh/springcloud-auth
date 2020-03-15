package com.wuwei.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuwei.auth.common.SysSocialDetails;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 第三方用户信息service
 */
public interface AuthenticationSocialUserService extends IService<SysSocialDetails> {

    UserDetails getSocialUserInfo(String key);
}
