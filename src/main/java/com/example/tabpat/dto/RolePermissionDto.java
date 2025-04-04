package com.example.tabpat.dto;

import com.example.tabpat.domain.AppDo;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 权限
 *
 * @author ABin
 * @date 2025/03/11
 */
@Data
@ToString
public class RolePermissionDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    List<AppDo> permissions;
}
