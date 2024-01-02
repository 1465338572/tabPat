package com.example.tabpat.dto;

import com.example.tabpat.domain.AppDo;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class AppDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private List<String> type;
    private String code;
    private String icon;
    private Long pid;
    private String path;

    public List<AppDto> children = new ArrayList<>();

    public void addChild(AppDto child) {
        this.children.add(child);
    }
}
