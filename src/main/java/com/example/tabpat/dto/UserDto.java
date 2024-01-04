package com.example.tabpat.dto;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.annotations.Lang;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class UserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
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
