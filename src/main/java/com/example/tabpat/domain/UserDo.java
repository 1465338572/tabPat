package com.example.tabpat.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.annotations.Lang;
import org.mapstruct.Mapper;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author ABin
 * @date 2025/03/11
 */
@Data
@ToString
@Mapper
public class UserDo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 用户id
    private String userId;
    private String username;
    private String password;
    private String qq;
    private String weChat;
    private String email;
    private String name;
    private Long birthDay;
    private Long createTime;
    private Long updateTime;
    private String phone;
    private String photo;
}
