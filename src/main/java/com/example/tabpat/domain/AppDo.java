package com.example.tabpat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ABin
 */
@Data
@ToString
@TableName("apps")
public class AppDo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    private String name;
    private String type;
    private String code;
    private String icon;
    private Long pid;
    private String path;
    private String pos;

    public List<AppDo> children = new ArrayList<>();
}
