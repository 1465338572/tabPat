package com.example.tabpat.service;

import com.example.tabpat.domain.AppDo;
import com.example.tabpat.domain.RolePermissionDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.domain.UserRoleDo;
import com.example.tabpat.dto.AppDto;
import com.google.protobuf.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppService extends BaseService {
    @Transactional
    public Result list() throws ServiceException {
        try {
            //先获取用户的角色
            //获取登录用户
            UserDo userDo = userDao.getUserByName(getCurrentUsername());
            //获取登录用户的角色Id列表
            List<UserRoleDo> userRoleDoList = userRoleDao.selectByUserId(userDo.getUserId());
            //获取该用户下所有的角色id
            List<String> roleIds = userRoleDoList.stream()
                    .map(UserRoleDo :: getRoleId)
                    .toList();
            //根据用户的角色id获取按钮路由信息
            List<RolePermissionDo> rolePermissionDoList = rolePermissionDao.getRolePermissionByRoleIds(roleIds);

            List<String> permissionIds = rolePermissionDoList.stream()
                    .map(RolePermissionDo :: getPermissionId)
                    .toList();

            List<AppDo> appDoList = appDao.selectByAppIds(permissionIds);
            List<AppDto> appDtoList = getAppDto(appDoList);
            return Result.success(200, "路由查询成功", appDtoList);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private List<AppDto> getAppDto(List<AppDo> appDos) {
        List<AppDto> appDtos = new ArrayList<>();
        Map<Long, AppDto> appDtoMap = new HashMap<>();
        for (AppDo appDo : appDos) {
            AppDto appDto = getAppDto(appDo);
            appDtos.add(appDto);

            appDtoMap.put(appDo.getId(), appDto);
        }

        for (AppDto appDto : appDtos) {
            if (appDto.getPid() != null && appDto.getPid() != 0) {
                AppDto parent = appDtoMap.get(appDto.getPid());
                if (parent != null) {
                    parent.addChild(appDto);
                }
            }
        }

        return appDtos.stream().filter(appDto -> appDto.getPid() == null || appDto.getPid() == 0).collect(Collectors.toList());
    }

    private static AppDto getAppDto(AppDo appDo) {
        AppDto appDto = new AppDto();
        appDto.setId(appDo.getId());
        appDto.setName(appDo.getName());
        appDto.setType(appDo.getType());
        appDto.setCode(appDo.getCode());
        appDto.setIcon(appDo.getIcon());
        appDto.setPid(appDo.getPid());
        appDto.setPath(appDo.getPath());
        if (appDo.getPos() != null) {
            String[] posArray = appDo.getPos().split(",");
            List<String> posList = new ArrayList<>(Arrays.asList(posArray));
            appDto.setPos(posList);
        }
        return appDto;
    }
}