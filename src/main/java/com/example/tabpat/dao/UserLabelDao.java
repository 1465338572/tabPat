package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.UserLabelDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLabelDao extends BaseMapper<UserLabelDo> {
    UserLabelDo getUserLabelByUserId(@Param("labelId") String labelId);

}
