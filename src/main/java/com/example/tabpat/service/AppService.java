package com.example.tabpat.service;

import com.example.tabpat.domain.AppDo;
import com.example.tabpat.dto.AppDto;
import com.google.protobuf.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppService extends BaseService{
    @Transactional
    public Result list() throws ServiceException {
        try{
            List<AppDo> appDoList  = appDao.selectList(null);
            List<AppDto> appDtoList = getAppDto(appDoList);
            return Result.success(200,"路由查询成功",appDtoList);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    private List<AppDto> getAppDto(List<AppDo> appDos){
        List<AppDto> appDtos = new ArrayList<>();
        Map<Long, AppDto> appDtoMap = new HashMap<>();
        for (AppDo appDo : appDos){
            AppDto appDto = getAppDto(appDo);
            appDtos.add(appDto);

            appDtoMap.put(appDo.getId(),appDto);
        }

        for (AppDto appDto : appDtos){
            if (appDto.getPid() != null && appDto.getPid() != 0){
                AppDto parent = appDtoMap.get(appDto.getPid());
                if (parent != null){
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
        String[] typeArray = appDo.getType().split(",");
        List<String> typeList = new ArrayList<>(Arrays.asList(typeArray));
        appDto.setType(typeList);
        appDto.setCode(appDo.getCode());
        appDto.setIcon(appDo.getIcon());
        appDto.setPid(appDo.getPid());
        appDto.setPath(appDo.getPath());
        if (appDo.getPos() != null){
            String[] posArray = appDo.getPos().split(",");
            List<String> posList = new ArrayList<>(Arrays.asList(posArray));
            appDto.setPos(posList);
        }
        return appDto;
    }
}