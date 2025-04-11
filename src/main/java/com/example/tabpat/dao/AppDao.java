package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.AppDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ABin
 */
@Repository
public interface AppDao extends BaseMapper<AppDo> {
    AppDo selectByAppId(@Param("appId") String appId);
    List<AppDo> selectByAppIds(@Param("appIds") List<String> appIds);
}
