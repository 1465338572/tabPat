package com.example.tabpat.web;

import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.LoginForm;
import com.example.tabpat.form.UserForm;
import com.example.tabpat.query.UserQuery;
import com.example.tabpat.service.LoginService;
import com.example.tabpat.service.Result;
import com.example.tabpat.service.UserService;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 用户web
 * @author ABin
 * @date 2025/03/11
 */
@RestController
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

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
            return Result.success(HttpStatusCode.OK, "登录成功", map);
        } catch (RuntimeException e) {
            logger.error("用户登录失败",e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            return Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/public/getUser")
    @ResponseBody
    public Result getUser(@RequestBody UserQuery userQuery,HttpServletResponse response) {
        Result result;
        try {
            result =userService.getUser(userQuery);
            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @PostMapping(value = "/public/addUser")
    @ResponseBody
    public Result save(@RequestBody UserForm userForm,HttpServletResponse response) throws ServiceException {
        Result result;
        try {
            result =userService.save(userForm);
            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            logger.error("用户注册失败",e);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @PutMapping(value = "/secure/updateUser")
    @ResponseBody
    public Result update(@RequestBody UserForm userForm,HttpServletResponse response) {
        Result result;
        try {
            result = userService.update(userForm);
            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }

        return result;
    }

    @PutMapping(value = "/public/updatePassword")
    @ResponseBody
    public Result updatePassword(@RequestBody UserForm userForm,HttpServletResponse response) {
        Result result;
        try {
            result = userService.updatePassword(userForm);
            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }

        return result;
    }
}
