package com.example.tabpat.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色权限对照
 *
 * @author ABin
 * @date 2025/03/11
 */
@Data
@ToString
public class RolePermissionDo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //角色Id
    private String roleId;
    //权限Id
    private String permissionId;
}
