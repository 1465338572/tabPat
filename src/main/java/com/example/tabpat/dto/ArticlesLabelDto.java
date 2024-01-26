package com.example.tabpat.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class ArticlesLabelDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer articleCount;
}
