package com.example.tabpat.web;

import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.RolePermissionForm;
import com.example.tabpat.service.Result;
import com.example.tabpat.service.RolePermissionService;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色权限web
 *
 * @author ABin
 * @date 2025/03/11
 */
@RestController
public class RolePermissionController {

    private static final Logger logger = LogManager.getLogger(RolePermissionController.class);


    private RolePermissionService rolePermissionService;

    @Autowired
    public void setRolePermissionService(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    /**
     * 根据角色Id获取权限列表
     *
     * @param roleId
     * @param response
     * @return {@link Result }
     */
    @GetMapping(value = "/secure/getRolePermission/{role_id}")
    @ResponseBody
    public Result get(@PathVariable("role_id") String roleId, HttpServletResponse response) {
        Result result;
        try {
            result = rolePermissionService.get(roleId);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            logger.error("角色权限获取失败", e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @PostMapping(value = "/secure/addRolePermission")
    @ResponseBody
    public Result save(@RequestBody RolePermissionForm rolePermissionForm, HttpServletResponse response) {
        Result result;
        try {
            result = rolePermissionService.save(rolePermissionForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            logger.error("角色权限添加失败", e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @DeleteMapping(value = "/secure/deleteRolePermission")
    @ResponseBody
    public Result delete(@RequestBody RolePermissionForm rolePermissionForm, HttpServletResponse response) {
        Result result;
        try {
            result = rolePermissionService.delete(rolePermissionForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            logger.error("角色权限删除失败", e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }
}
