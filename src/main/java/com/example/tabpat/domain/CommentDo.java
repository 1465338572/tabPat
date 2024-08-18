package com.example.tabpat.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 评论
 */
@Data
@ToString
public class CommentDo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 评论id
     */
    private String commentId;
    /**
     * 评论
     */
    private String content;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 是否被删除
     */
    private Boolean isDelete;
    /**
     * 评论者名称（唯一）
     */
    private String username;
    /**
     * 博客id
     */
    private String articleId;
    /**
     * 点赞数量
     */
    private Integer commentLikeCount;
    /**
     * 顶级评论id 不为空则为顶级评论id，为空则是顶级评论
     */
    private String rootCommentId;
    /**
     * 回复目标评论id，为空则为顶级评论，不为空则为目标评论id
     */
    private String toCommentId;
}
