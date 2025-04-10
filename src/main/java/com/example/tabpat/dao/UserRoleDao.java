package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.UserLabelDo;
import com.example.tabpat.domain.UserRoleDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ABin
 * @date 2025/04/10
 */
@Repository
public interface UserRoleDao extends BaseMapper<UserRoleDo> {
    List<UserRoleDo> selectByUserId(@Param("userId") String userId);
    void remove(@Param("userId") String userId);
    void insertBatch(@Param("userRoleDoList") List<UserRoleDo> userRoleDoList);
}
