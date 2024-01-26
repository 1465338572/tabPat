package com.example.tabpat.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class ArticlesLabelForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String labelId;

    private String articlesId;

    /**
     * 批量的id，删除使用
     */
    private List<String> articlesIds;
}
