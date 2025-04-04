package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.RolePermissionDo;
import com.example.tabpat.form.RolePermissionForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ABin
 * @date 2025/03/11
 */
@Repository
public interface RolePermissionDao extends BaseMapper<RolePermissionDo> {
    List<RolePermissionDo> getRolePermissionByRoleId(@Param("roleId") String roleId);

    List<RolePermissionDo> getRolePermissionByPermissionId(@Param("permissionId") String permissionId);

    void deleteRolePermission(RolePermissionDo rolePermissionDo);
}
