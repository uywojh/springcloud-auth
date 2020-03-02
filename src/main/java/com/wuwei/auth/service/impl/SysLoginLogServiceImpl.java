package com.wuwei.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuwei.auth.entity.SysLoginLog;
import com.wuwei.auth.mapper.SysLoginLogMapper;
import com.wuwei.auth.service.SysLoginLogService;
import org.springframework.stereotype.Service;


@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {
	
}
