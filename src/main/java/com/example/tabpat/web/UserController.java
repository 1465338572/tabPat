package com.example.tabpat.web;

import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.form.LoginForm;
import com.example.tabpat.form.UserForm;
import com.example.tabpat.service.LoginService;
import com.example.tabpat.service.Result;
import com.example.tabpat.service.UserService;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class UserController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    /**
     * 登录返回token
     */
    @PostMapping(value = "/public/login")
    @ResponseBody
    public Result login(@RequestBody LoginForm loginForm, HttpServletResponse response) {

        try {
            Map map = loginService.login(loginForm);
            return Result.create(HttpStatusCode.OK, "登录成功", map);
        } catch (RuntimeException re) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            return Result.create(HttpStatusCode.SERVICEERROR, re.getMessage());
        }
    }

    @PostMapping(value = "/public/addUser")
    @ResponseBody
    public Result save(@RequestBody UserForm userForm,HttpServletResponse response) {

        try {
            return userService.save(userForm);
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            return Result.create(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
    }

    @PutMapping(value = "/secure/updateUser")
    @ResponseBody
    public Result update(@RequestBody UserForm userForm,HttpServletResponse response) {
        try {
            return userService.update(userForm);
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            return Result.create(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
    }
}
