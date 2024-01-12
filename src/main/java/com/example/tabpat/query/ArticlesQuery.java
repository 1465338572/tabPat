package com.example.tabpat.query;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ArticlesQuery {
    private String articlesTitle;
    private Integer pageNum;
    private Integer pageSize;
}
