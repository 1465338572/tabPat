package com.example.tabpat.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class ArticlesLabelDo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String labelId;
    private String articlesId;

    private List<ArticlesDo> articlesDoList;

}
