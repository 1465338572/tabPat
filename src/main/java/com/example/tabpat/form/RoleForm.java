package com.example.tabpat.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 角色表单
 *
 * @author ABin
 * @date 2025/03/11
 */
@Data
@ToString
public class RoleForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //角色Id
    private String roleId;
    //角色名
    private String roleName;
    //描述
    private String description;

    /**
     * 批量的id，删除使用
     */
    private List<String> roleIds;
}
