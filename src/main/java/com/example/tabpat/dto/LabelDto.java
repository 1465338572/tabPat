package com.example.tabpat.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class LabelDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String labelId;


    private String labelName;

    private Integer articleCount;
}
