package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.ArticlesCountDo;
import com.example.tabpat.domain.ArticlesDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesCountDao extends BaseMapper<ArticlesCountDo> {
    ArticlesCountDo getLast(@Param("userId") String userId);

}
