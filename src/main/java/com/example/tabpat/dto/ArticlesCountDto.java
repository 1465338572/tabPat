package com.example.tabpat.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@ToString
@Data
public class ArticlesCountDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long date;
    private Integer likeCount;
}
