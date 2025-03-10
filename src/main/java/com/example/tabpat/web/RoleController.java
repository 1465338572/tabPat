package com.example.tabpat.web;

import com.example.tabpat.annotation.ArticlesView;
import com.example.tabpat.annotation.UnderlineToCamel;
import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.ArticlesForm;
import com.example.tabpat.form.RoleForm;
import com.example.tabpat.query.RoleQuery;
import com.example.tabpat.service.Result;
import com.example.tabpat.service.RoleService;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色web
 *
 * @author ABin
 * @date 2025/03/11
 */
@RestController
public class RoleController {

    private static final Logger logger = LogManager.getLogger(RoleController.class);

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


    /**
     * 获取所有角色
     *
     * @param roleQuery
     * @param response
     * @return {@link Result }
     */
    @GetMapping(value = "/secure/listRole")
    @ResponseBody
    public Result pubList(@UnderlineToCamel RoleQuery roleQuery, HttpServletResponse response) {
        Result result;
        try {
            result = roleService.list(roleQuery);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            logger.error("角色查询错误", e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());

        }
        return result;
    }

    /**
     * 获取单个角色的信息
     *
     * @param roleId
     * @param response
     * @return {@link Result }
     */
    @GetMapping(value = "/secure/getRole/{role_id}")
    @ResponseBody
    @ArticlesView
    public Result get(@PathVariable("role_id") String roleId, HttpServletResponse response) {
        Result result;
        try {
            result = roleService.get(roleId);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            logger.error("角色获取失败", e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    /**
     * 角色创建
     *
     * @param roleForm
     * @param response
     * @return {@link Result }
     */
    @PostMapping(value = "/secure/addRole")
    @ResponseBody
    public Result save(@RequestBody RoleForm roleForm, HttpServletResponse response) {
        Result result;
        try {
            result = roleService.save(roleForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            logger.error("角色保存失败", e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    /**
     * 角色更新
     *
     * @param roleForm
     * @param response
     * @return {@link Result }
     */
    @PutMapping(value = "/secure/updateRole")
    @ResponseBody
    public Result update(@RequestBody RoleForm roleForm, HttpServletResponse response) {
        Result result;
        try {
            result = roleService.update(roleForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            logger.error("角色更新失败", e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @DeleteMapping(value = "/secure/deleteRoles")
    @ResponseBody
    public Result delete(@RequestBody RoleForm roleForm, HttpServletResponse response) throws ServiceException {
        Result result;
        try {
            result = roleService.delete(roleForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            logger.error("角色删除失败", e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }
}
