package com.example.tabpat.service;

import com.example.tabpat.check.UserCheck;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.domain.UserRoleDo;
import com.example.tabpat.form.UserForm;
import com.example.tabpat.util.BeanCopierUtil;
import com.example.tabpat.util.PrimaryKeyUtil;
import com.example.tabpat.util.Utils;
import com.google.protobuf.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserService extends BaseService {
    private UserCheck userCheck;

    @Autowired
    public void setUserCheck(UserCheck userCheck) {
        this.userCheck = userCheck;
    }

    @Transactional
    public Result save(UserForm userForm) throws ServiceException {
        try {
            Result result = userCheck.checkSave(userForm);
            if (result.getCode() != 200) {
                return result;
            }
            UserDo userDo = buildUserSave(userForm);
            UserRoleDo userRoleDo = buildUserRoleSave(userDo);
            userDao.insert(userDo);
            userRoleDao.insert(userRoleDo);
            return Result.create(200, "用户创建成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    public Result update(UserForm userForm) throws ServiceException {
        try {
            Result result = userCheck.checkUpdate(userForm);
            if (result.getCode() != 200) {
                return result;
            }
            UserDo userDo = buildUserUpdate(userForm);
            userDao.updateById(userDo);
            return Result.create(200, "用户更新成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private UserDo buildUserSave(UserForm userForm) throws ServiceException {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            UserDo userDo = BeanCopierUtil.create(userForm, UserDo.class);
            String userId = PrimaryKeyUtil.get();
            userDo.setUserId(userId);
            userDo.setUsername(userForm.getUsername());
            String password = encoder.encode(userForm.getPassword());
            userDo.setPassword(password);
            userDo.setQq(userForm.getQq());
            userDo.setWeChat(userForm.getWeChat());
            userDo.setEmail(userForm.getEmail());
            userDo.setName(userForm.getName());
            userDo.setCreateTime(System.currentTimeMillis());
            userDo.setUpdateTime(System.currentTimeMillis());
            userDo.setBirthDay(Utils.createTimestamp(userForm.getBirthDay()));
            userDo.setPhone(userForm.getPhone());
            return userDo;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private UserDo buildUserUpdate(UserForm userForm) throws ServiceException {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String username = getCurrentUsername();
            UserDo dbUserDo = userDao.getUserByName(username);
            UserDo userDo = BeanCopierUtil.create(dbUserDo, UserDo.class);
            userDo.setUsername(username);
            if (StringUtils.hasText(userForm.getPassword())) {
                userDo.setPassword(encoder.encode(userForm.getPassword()));
            }
            if (StringUtils.hasText(userForm.getQq())) {
                userDo.setQq(userForm.getQq());
            }
            if (StringUtils.hasText(userForm.getWeChat())) {
                userDo.setWeChat(userForm.getWeChat());
            }
            if (StringUtils.hasText(userForm.getEmail())) {
                userDo.setEmail(userForm.getEmail());
            }
            if (StringUtils.hasText(userForm.getName())) {
                userDo.setName(userForm.getName());
            }
            userDo.setUpdateTime(System.currentTimeMillis());
            if (StringUtils.hasText(userForm.getBirthDay())) {
                userDo.setBirthDay(Utils.createTimestamp(userForm.getBirthDay()));
            }
            if (StringUtils.hasText(userForm.getPhone())) {
                userDo.setPhone(userForm.getPhone());
            }
            return userDo;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private UserRoleDo buildUserRoleSave(UserDo userDo) throws ServiceException {
        try {
            UserRoleDo userRoleDo = new UserRoleDo();
            userRoleDo.setRoleId(2);
            userRoleDo.setUserId(userDo.getUserId());
            return userRoleDo;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
