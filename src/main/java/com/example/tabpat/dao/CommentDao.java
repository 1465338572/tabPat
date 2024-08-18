package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.ArticlesDo;
import com.example.tabpat.domain.CommentDo;
import org.apache.ibatis.annotations.Param;

public interface CommentDao extends BaseMapper<CommentDo> {
    ArticlesDo getCommentByCommentId(@Param("commentId") String commentId);
}
