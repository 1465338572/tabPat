package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.RoleDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ABin
 * @date 2025/03/11
 */
@Repository
public interface RoleDao extends BaseMapper<RoleDo> {
    List<RoleDo> getRoleById(@Param("roleId") String roleId);

    RoleDo getRoleByName(@Param("roleName") String roleName);
}