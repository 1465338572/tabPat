package com.example.tabpat.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class AppDo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String type;
    private String code;
    private String icon;
    private Long pid;
    private String path;
    private String pos;

    public List<AppDo> children = new ArrayList<>();

    public void addChild(AppDo child) {
        this.children.add(child);
    }
}
