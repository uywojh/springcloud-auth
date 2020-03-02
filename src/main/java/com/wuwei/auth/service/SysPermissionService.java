package com.wuwei.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuwei.auth.common.PermissionEntityVO;
import com.wuwei.auth.entity.SysPermission;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {

    void updateSysPermission(List<PermissionEntityVO> permission);
}
