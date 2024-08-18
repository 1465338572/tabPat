package com.example.tabpat.query;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class CommentQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer pageNum;
    private Integer pageSize;
}
