package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.LabelDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelDao extends BaseMapper<LabelDo> {
    LabelDo getLabelByLabelName(@Param("userId") String userId,@Param("labelName") String labelName);
    LabelDo getLabelByLabelId(@Param("userId") String userId,@Param("labelId") String labelId);

    List<LabelDo> selectAll(@Param("userId") String userId,@Param("labelName") String labelName);

}
