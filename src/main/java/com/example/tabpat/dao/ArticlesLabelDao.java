package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.ArticlesDo;
import com.example.tabpat.domain.ArticlesLabelDo;
import com.example.tabpat.domain.LabelDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticlesLabelDao extends BaseMapper<ArticlesLabelDo> {
    ArticlesLabelDo getArticlesLabel(@Param("articlesId") String articlesId, @Param("labelId") String labelId);
    List<ArticlesLabelDo> selectArticlesLabel(@Param("labelId") String labelId);

    int getCountById(@Param("labelId") String labelId);

}