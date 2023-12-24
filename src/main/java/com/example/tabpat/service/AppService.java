package com.example.tabpat.service;

import com.example.tabpat.domain.AppDo;
import com.google.protobuf.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppService extends BaseService{
    @Transactional
    public Result list() throws ServiceException {
        try{
            List<AppDo> list  = appDao.selectList(null);
            Map<Long, AppDo> appDoMap = new HashMap<>();

            for (AppDo appDo : list) {
                appDoMap.put(appDo.getId(), appDo);
            }

            for (AppDo appDo : list) {
                if (appDo.getPid() != 0) {
                    AppDo parent = appDoMap.get(appDo.getPid());
                    if (parent != null) {
                        parent.addChild(appDo);
                    }
                }
            }

            List<AppDo> topLevelApps = new ArrayList<>();
            for (AppDo appDo : list) {
                if (appDo.getPid() == 0) {
                    topLevelApps.add(appDo);
                }
            }

            return Result.create(200,"路由查询成功",topLevelApps);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }
}
