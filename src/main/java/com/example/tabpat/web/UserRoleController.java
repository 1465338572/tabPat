package com.example.tabpat.web;

import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.UserRoleForm;
import com.example.tabpat.service.Result;
import com.example.tabpat.service.UserRoleService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ABin
 * @date 2025/04/10
 */
@RestController
public class UserRoleController {
    private static final Logger logger = LogManager.getLogger(UserRoleController.class);

    private UserRoleService userRoleService;

    @Autowired
    public void setUserRoleService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping(value="/secure/getUserRole/{userId}")
    @ResponseBody
    public Result getUserRole(@PathVariable String userId, HttpServletResponse response) {
        Result result;
        try {
            result = userRoleService.getUserRole(userId);
            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        }catch (Exception e) {
            logger.error(e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @PostMapping(value="/secure/addUserRole")
    @ResponseBody
    public Result addUserRole(@RequestBody UserRoleForm userRoleForm, HttpServletResponse response) {
        Result result;
        try {
            result = userRoleService.save(userRoleForm);
            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        }catch (Exception e){
            logger.error(e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }
}
