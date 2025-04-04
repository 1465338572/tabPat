package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.AppDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author ABin
 */
@Repository
public interface AppDao extends BaseMapper<AppDo> {
    AppDo selectByAppId(@Param("appId") String appId);
}
