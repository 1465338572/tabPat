package com.example.tabpat.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class ArticlesCountDo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long date;
    private String userId;
    private Integer likeCount;
}
