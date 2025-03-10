package com.example.tabpat.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色返回类
 *
 * @author ABin
 * @date 2025/03/11
 */
@Data
@ToString
public class RoleDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //角色id
    private String roleId;
    //角色名
    private String roleName;
    //创建时间
    private Long createTime;
    //更新时间
    private Long updateTime;
    //描述
    private String description;
}
