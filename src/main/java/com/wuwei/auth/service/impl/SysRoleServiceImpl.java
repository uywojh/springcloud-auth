package com.wuwei.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuwei.auth.common.SysRole;
import com.wuwei.auth.mapper.SysRoleMapper;
import com.wuwei.auth.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 角色
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2019年7月30日 下午22:33:33
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

}
