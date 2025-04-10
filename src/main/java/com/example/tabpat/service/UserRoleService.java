package com.example.tabpat.service;

import com.example.tabpat.domain.UserRoleDo;
import com.example.tabpat.dto.UserRoleDto;
import com.example.tabpat.form.UserRoleForm;
import com.google.protobuf.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ABin
 * @date 2025/04/09
 */
@Service
public class UserRoleService extends BaseService{

    private static final Logger logger = LogManager.getLogger(UserRoleService.class);

    @Transactional
    public Result getUserRole(String userId) throws ServiceException {
        try {

            List<UserRoleDo> userRoleDoList = userRoleDao.selectByUserId(userId);
            UserRoleDto userRoleDto = buildUserRoleGet(userRoleDoList);

            return Result.success(200,"查询成功",userRoleDto);
        }catch (Exception e){
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    private UserRoleDto buildUserRoleGet(List<UserRoleDo> userRoleDoList){
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDto.setUserId(userRoleDoList.get(0).getUserId());
        List<String> roleIds = userRoleDoList.stream()
                .map(UserRoleDo :: getRoleId)
                .toList();
        userRoleDto.setRoleList(roleIds);
        return userRoleDto;
    }

    @Transactional
    public Result save(UserRoleForm userRoleForm) throws ServiceException{
        try {
            userRoleDao.remove(userRoleForm.getUserId());
            List<UserRoleDo> userRoles = buildUserRoleSave(userRoleForm);
            userRoleDao.insertBatch(userRoles);

            return Result.success(200,"保存成功");
        }catch(Exception e){
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    private List<UserRoleDo> buildUserRoleSave(UserRoleForm userRoleForm){
        String userId = userRoleForm.getUserId();
        List<String> roleIds = userRoleForm.getRoleList();
        return roleIds.stream().map(roleId -> {
            UserRoleDo userRole = new UserRoleDo();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            return userRole;
        }).toList();
    }
}
