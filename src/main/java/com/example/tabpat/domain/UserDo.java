package com.example.tabpat.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.annotations.Lang;

@Data
@ToString
public class UserDo {
    private String userId; // 用户id
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

}
