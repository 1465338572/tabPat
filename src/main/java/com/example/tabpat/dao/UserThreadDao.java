package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.UserThreadDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserThreadDao extends BaseMapper<UserThreadDo> {
    UserThreadDo getThreadByUserId(@Param("userId") String userId);

}
