package com.example.tabpat.form;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserForm {
    private String username;
    private String password;
    private String qq;
    private String weChat;
    private String email;
    private String name;
    private String birthDay;
    private String phone;
}
