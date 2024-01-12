package com.example.tabpat.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
@Data
@ToString
public class ArticlesDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 博文id
     */
    private String articleId;
    /**
     * 博文标题
     */
    private String articleTitle;
    /**
     * 博文内容
     */
    private String articleContent;
    /**
     * 浏览量
     */
    private Integer articleView;
    /**
     * 发表时间
     */
    private Long articleDate;
    /**
     * 点赞数
     */
    private Integer articleLikeCount;
    /**
     * 文章是否展示
     */
    private Boolean articleShow;
}
