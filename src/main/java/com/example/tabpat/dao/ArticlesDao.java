package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.AppDo;
import com.example.tabpat.domain.ArticlesDo;
import com.example.tabpat.domain.UserDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesDao extends BaseMapper<ArticlesDo> {
    ArticlesDo getArticlesByArticleId(@Param("articleId") String articleId);
}
