package com.wuwei.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuwei.auth.common.SysSocialDetails;
import com.wuwei.auth.entity.UserInfo;

public interface AuthenticationSocialUserMapper extends BaseMapper<SysSocialDetails> {

    UserInfo getSocialUserInfo(String key);
}
