package com.example.tabpat.service;

import com.example.tabpat.domain.AppDo;
import com.example.tabpat.domain.RolePermissionDo;
import com.example.tabpat.dto.RolePermissionDto;
import com.example.tabpat.form.RolePermissionForm;
import com.example.tabpat.util.BeanCopierUtil;
import com.google.protobuf.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限
 *
 * @author ABin
 * @date 2025/03/11
 */
@Service
public class RolePermissionService extends BaseService {
    private static final Logger logger = LogManager.getLogger(RolePermissionService.class);

    @Transactional
    public Result get(String roleId) throws ServiceException {
        try {

            List<RolePermissionDo> rolePermissionDoList = rolePermissionDao.getRolePermissionByRoleId(roleId);
            RolePermissionDto rolePermissionDto = buildRolePermissionGet(rolePermissionDoList);
            return Result.success(200, "角色权限查询成功", rolePermissionDto);
        } catch (Exception e) {
            logger.error("角色权限查询失败", e);
            throw new ServiceException(e);
        }
    }

    private RolePermissionDto buildRolePermissionGet(List<RolePermissionDo> rolePermissionDoList) {
        RolePermissionDto rolePermissionDto = new RolePermissionDto();

        List<AppDo> permissions = new ArrayList<>();
        for (RolePermissionDo rolePermissionDo : rolePermissionDoList) {
            AppDo appDo = appDao.selectByAppId(rolePermissionDo.getPermissionId());
            permissions.add(appDo);
        }
        rolePermissionDto.setPermissions(permissions);
        return rolePermissionDto;
    }

    @Transactional
    public Result save(RolePermissionForm rolePermissionForm) throws ServiceException {
        try {
            RolePermissionDo rolePermissionDo = buildRolePermissionSave(rolePermissionForm);
            rolePermissionDao.insert(rolePermissionDo);
            return Result.success(200, "角色权限保存成功");
        } catch (Exception e) {
            logger.error("角色权限保存失败", e);
            throw new ServiceException(e);
        }
    }

    private RolePermissionDo buildRolePermissionSave(RolePermissionForm rolePermissionForm) {
        RolePermissionDo rolePermissionDo = BeanCopierUtil.create(rolePermissionForm, RolePermissionDo.class);
        rolePermissionDo.setRoleId(rolePermissionForm.getRoleId());
        rolePermissionDo.setPermissionId(rolePermissionForm.getPermissionId());
        return rolePermissionDo;
    }

    @Transactional
    public Result delete(RolePermissionForm rolePermissionForm) throws ServiceException {
        try {
            RolePermissionDo rolePermissionDo = buildRolePermissionDelete(rolePermissionForm);
            rolePermissionDao.deleteRolePermission(rolePermissionDo);
            return Result.success(200, "角色权限删除成功");
        } catch (Exception e) {
            logger.error("角色权限删除失败", e);
            throw new ServiceException(e);
        }
    }

    private RolePermissionDo buildRolePermissionDelete(RolePermissionForm rolePermissionForm) {
        RolePermissionDo rolePermissionDo = BeanCopierUtil.create(rolePermissionForm, RolePermissionDo.class);
        rolePermissionDo.setRoleId(rolePermissionForm.getRoleId());
        rolePermissionDo.setPermissionId(rolePermissionForm.getPermissionId());
        return rolePermissionDo;
    }
}
