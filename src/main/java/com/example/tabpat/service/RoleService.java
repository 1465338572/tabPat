package com.example.tabpat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tabpat.domain.RoleDo;
import com.example.tabpat.dto.RoleDto;
import com.example.tabpat.form.RoleForm;
import com.example.tabpat.query.RoleQuery;
import com.example.tabpat.util.BeanCopierUtil;
import com.example.tabpat.util.PrimaryKeyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.protobuf.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色
 *
 * @author ABin
 * @date 2025/03/11
 */

@Service
public class RoleService extends BaseService {
    private static final Logger logger = LogManager.getLogger(RoleService.class);

    //获取全部角色或者根据条件查询
    @Transactional
    public Result list(RoleQuery roleQuery) throws ServiceException {
        try {
            QueryWrapper<RoleDo> wrapper = new QueryWrapper<>();
            if (roleQuery.getRoleId() != null) {
                wrapper.eq("role_id", roleQuery.getRoleId());
            }
            if (roleQuery.getRoleName() != null) {
                wrapper.like("role_name", roleQuery.getRoleName());
            }
            wrapper.orderByDesc("create_time");
            PageHelper.startPage(roleQuery.getPageNum(), roleQuery.getPageSize());
            List<RoleDo> roleDoList = roleDao.selectList(wrapper);
            PageInfo<RoleDo> roleDoPageInfo = new PageInfo<>(roleDoList);
            List<RoleDto> roleDtoList = new ArrayList<>();
            if (roleDoList.isEmpty()) {
                return Result.success(200, "获取角色成功", roleDtoList);
            }
            for (RoleDo roleDo : roleDoList) {
                RoleDto roleDto = listShowDto(roleDo);
                roleDtoList.add(roleDto);
            }

            PageInfo<RoleDto> roleDtoPageInfo = new PageInfo<>(roleDtoList);
            roleDtoPageInfo.setTotal(roleDoPageInfo.getTotal());
            return Result.success(200, "获取成功", roleDtoPageInfo);
        } catch (Exception e) {
            logger.error("查询角色列表失败", e);
            throw new ServiceException(e);
        }
    }

    private RoleDto listShowDto(RoleDo roleDo) {
        RoleDto roleDto = new RoleDto();
        roleDto.setRoleId(roleDo.getRoleId());
        roleDto.setRoleName(roleDo.getRoleName());
        roleDto.setCreateTime(roleDo.getCreateTime());
        roleDto.setUpdateTime(roleDo.getUpdateTime());
        roleDto.setDescription(roleDo.getDescription());
        return roleDto;
    }

    //根据id查询角色
    @Transactional
    public Result get(String roleId) throws ServiceException {
        try {
            RoleDo roleDo = roleDao.selectById(roleId);
            RoleDto roleDto = getShowDto(roleDo);
            return Result.success(200, "获取成功", roleDto);
        } catch (Exception e) {
            logger.error("获取角色失败", e);
            throw new ServiceException(e);
        }
    }

    private RoleDto getShowDto(RoleDo roleDo) {
        RoleDto roleDto = new RoleDto();
        roleDto.setRoleId(roleDo.getRoleId());
        roleDto.setRoleName(roleDo.getRoleName());
        roleDto.setCreateTime(roleDo.getCreateTime());
        roleDto.setUpdateTime(roleDo.getUpdateTime());
        roleDto.setDescription(roleDo.getDescription());
        return roleDto;
    }

    //创建角色
    @Transactional
    public Result save(RoleForm roleForm) throws ServiceException {
        try {
            RoleDo roleDo = buildRoleSave(roleForm);
            roleDao.insert(roleDo);
            return Result.success(200, "角色创建成功");
        } catch (Exception e) {
            logger.error("创建角色失败", e);
            throw new ServiceException(e);
        }
    }

    private RoleDo buildRoleSave(RoleForm roleForm) {
        RoleDo roleDo = BeanCopierUtil.create(roleForm, RoleDo.class);
        String roleId = PrimaryKeyUtil.get();
        roleDo.setRoleId(roleId);
        roleDo.setRoleName(roleForm.getRoleName());
        roleDo.setCreateTime(System.currentTimeMillis());
        roleDo.setUpdateTime(System.currentTimeMillis());
        roleDo.setDescription(roleForm.getDescription());
        return roleDo;
    }

    //更新角色
    @Transactional
    public Result update(RoleForm roleForm) throws ServiceException {
        try {
            RoleDo roleDo = buildRoleUpdate(roleForm);
            roleDao.updateById(roleDo);
            return Result.success(200, "角色更新成功");
        } catch (Exception e) {
            logger.error("角色更新失败", e);
            throw new ServiceException(e);
        }
    }

    private RoleDo buildRoleUpdate(RoleForm roleForm) {
        RoleDo roleDo = BeanCopierUtil.create(roleForm, RoleDo.class);
        if (StringUtils.hasLength(roleForm.getRoleName())) {
            roleDo.setRoleName(roleForm.getRoleName());
        }
        if (StringUtils.hasLength(roleForm.getDescription())) {
            roleDo.setDescription(roleForm.getDescription());
        }
        roleDo.setUpdateTime(System.currentTimeMillis());
        return roleDo;
    }

    //删除角色
    @Transactional
    public Result delete(RoleForm roleForm) throws ServiceException {
        try {
            List<String> roleIds = roleForm.getRoleIds();

            for (String roleId : roleIds) {
                roleDao.deleteById(roleId);
            }
            return Result.success(200, "角色删除成功");
        } catch (Exception e) {
            logger.error("角色删除失败", e);
            throw new ServiceException(e);
        }
    }

}
