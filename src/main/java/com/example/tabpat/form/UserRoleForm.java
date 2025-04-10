package com.example.tabpat.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author ABin
 * @date 2025/04/10
 */
@Data
@ToString
public class UserRoleForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;
    private List<String> roleList;
}
