package com.example.tabpat.query;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色查询
 *
 * @author ABin
 * @date 2025/03/11
 */
@Data
@ToString
public class RoleQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //角色id
    private Integer roleId;
    //角色名
    private String roleName;
    //查询页码
    private Integer pageNum;
    //查询大小
    private Integer pageSize;
}
