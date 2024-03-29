package com.example.tabpat.check;

import com.example.tabpat.domain.UserDo;
import com.example.tabpat.form.UserForm;
import com.example.tabpat.service.BaseService;
import com.example.tabpat.service.Result;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.example.tabpat.code.HttpStatusCode.OK;
import static com.example.tabpat.code.HttpStatusCode.SERVICEERROR;

@Service
public class UserCheck extends BaseService {
    public Result checkSave(UserForm userForm) {
        return checkSaveNonLogic(userForm);
    }

    private Result checkSaveNonLogic(UserForm userForm) {
        if (!StringUtils.hasText(userForm.getUsername())) {
            return Result.create(SERVICEERROR, "用户名不能为空");
        }
        if (!StringUtils.hasText(userForm.getPassword())) {
            return Result.create(SERVICEERROR, "密码不能为空");
        }
        if (!StringUtils.hasText(userForm.getName())) {
            return Result.create(SERVICEERROR, "昵称不能空");
        }
        if (StringUtils.hasText(userForm.getEmail()) && !isVaildEmail(userForm.getEmail())) {
            return Result.create(SERVICEERROR, "邮箱格式错误");
        }
        if (StringUtils.hasText(userForm.getPhone()) && !isValidMobileNo(userForm.getPhone())) {
            return Result.create(SERVICEERROR, "手机号码格式错误");
        }
        UserDo userDo = userDao.getUserByName(userForm.getUsername());
        if (userDo != null) {
            return Result.create(SERVICEERROR, "用户名重复");
        }
        return Result.create(OK, "");
    }

    public Result checkUpdate(UserForm userForm) {
        return checkUpdateNonLogic(userForm);
    }

    private Result checkUpdateNonLogic(UserForm userForm) {
        if (StringUtils.hasText(userForm.getEmail()) && !isVaildEmail(userForm.getEmail())) {
            return Result.create(SERVICEERROR, "邮箱格式错误");
        }
        if (StringUtils.hasText(userForm.getPhone()) && !isValidMobileNo(userForm.getPhone())) {
            return Result.create(SERVICEERROR, "手机号码格式错误");
        }
        return Result.create(OK, "");
    }
}
