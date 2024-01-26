package com.example.tabpat.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class ArticlesForm implements Serializable {
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
     * 文章是否展示
     */
    private Boolean articleShow;
    /**
     * 文章封面
     */
    private String articleImg;
    /**
     * 标签id
     */
    private String labelId;
    /**
     * 批量的id，删除使用
     */
    private List<String> articleIds;

}
