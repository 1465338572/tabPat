package com.example.tabpat.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class RoleDo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer roleId;//角色id
    private String roleName;//角色名
}
