package com.example.tabpat.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author ABin
 * @date 2025/03/11
 */
@Data
@ToString
public class RolePermissionForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String roleId;
    private String permissionId;
}
