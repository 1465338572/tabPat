package com.example.tabpat.service;

import com.example.tabpat.check.UserCheck;
import com.example.tabpat.domain.RoleDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.domain.UserRoleDo;
import com.example.tabpat.dto.UserDto;
import com.example.tabpat.form.UserForm;
import com.example.tabpat.query.UserQuery;
import com.example.tabpat.util.BeanCopierUtil;
import com.example.tabpat.util.PrimaryKeyUtil;
import com.example.tabpat.util.Utils;
import com.google.protobuf.ServiceException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.IOException;
import java.nio.file.Paths;


/**
 * 用户服务
 *
 * @author ABin
 * @date 2025/03/10
 */
@Service
public class UserService extends BaseService {

    private static final Logger logger = LogManager.getLogger(UserService.class);


    private UserCheck userCheck;


    @Autowired
    public void setUserCheck(UserCheck userCheck) {
        this.userCheck = userCheck;
    }

    @Transactional
    public Result getUser(UserQuery userQuery) throws ServiceException {
        try {
            Result result = userCheck.checkUsername(userQuery.getUsername());
            if (result.getCode() != 200) {
                return result;
            }
            UserDo userDo = userDao.getUserByName(userQuery.getUsername());
            UserDto userDto = buildUserGet(userDo);
            return Result.success(200, "用户查询成功", userDto);
        } catch (Exception e) {
            logger.error("用户查询失败", e);
            throw new ServiceException(e);
        }
    }

    @Transactional(rollbackFor = ServiceException.class)
    public Result save(UserForm userForm) throws ServiceException {
        try {
            Result result = userCheck.checkSave(userForm);
            if (result.getCode() != 200) {
                return result;
            }
            //创建用户id，并以此创建文件夹来存储用户文件
            String userId = PrimaryKeyUtil.get();
            //获取当前路径
            Path currentPath = Paths.get(System.getProperty("user.dir"));
            Path targetPath = currentPath.resolve(userId);
            //创建目录
            Files.createDirectories(targetPath);
            String dirPath = targetPath.toAbsolutePath().toString();
            //存储用户数据
            UserDo userDo = buildUserSave(userForm, userId, dirPath);
            userDao.insert(userDo);
            //存储用户角色
            UserRoleDo userRoleDo = buildUserRoleSave(userDo);
            userRoleDao.insert(userRoleDo);
            return Result.success(200, "用户创建成功");
        } catch (Exception e) {
            logger.error("用户创建失败", e);
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
            return Result.success(200, "用户更新成功");
        } catch (Exception e) {
            logger.error("用户更新失败", e);
            throw new ServiceException(e);
        }
    }

    private UserDto buildUserGet(UserDo userDo) throws IOException {

        String photo = FileUtils.fileRead(userDo.getPhoto());
        UserDto userDto = BeanCopierUtil.create(userDo, UserDto.class);
        userDto.setUsername(userDo.getUsername());
        userDto.setEmail(userDo.getEmail());
        userDto.setBirthDay(userDo.getBirthDay());
        userDto.setCreateTime(userDo.getCreateTime());
        userDto.setUpdateTime(userDo.getUpdateTime());
        userDto.setPhone(userDo.getPhone());
        userDto.setPhoto(photo);

        return userDto;
    }

    @Transactional
    public Result updatePassword(UserForm userForm) throws ServiceException {
        try {
            Result result = userCheck.checkUsername(userForm.getUsername());
            if (result.getCode() != 200) {
                return result;
            }
            UserDo userDo = userDao.getUserByName(userForm.getUsername());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = encoder.encode(userForm.getPassword());
            userDo.setPassword(password);
            userDao.updateById(userDo);
            return Result.success(200, "用户更新成功");
        } catch (Exception e) {
            logger.error("用户注册失败", e);
            throw new ServiceException(e);
        }
    }

    private UserDo buildUserSave(UserForm userForm, String userId, String dirPath) throws ServiceException {
        try {

            //头像图片路径
            String imgPath = dirPath + "/" + System.currentTimeMillis() + "img.txt";
            FileUtils.fileWrite(imgPath, userForm.getPhoto());

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            UserDo userDo = BeanCopierUtil.create(userForm, UserDo.class);
            userDo.setUserId(userId);
            userDo.setUsername(userForm.getUsername());
            String password = encoder.encode(userForm.getPassword());
            userDo.setPassword(password);
            if (StringUtils.hasLength(userForm.getQq())) {
                userDo.setQq(userForm.getQq());
            }
            if (StringUtils.hasLength(userForm.getWeChat())) {
                userDo.setWeChat(userForm.getWeChat());
            }
            if (StringUtils.hasLength(userForm.getEmail())) {
                userDo.setEmail(userForm.getEmail());
            }
            userDo.setName(userForm.getName());
            userDo.setCreateTime(System.currentTimeMillis());
            userDo.setUpdateTime(System.currentTimeMillis());
            if (StringUtils.hasLength(userForm.getBirthDay())) {
                userDo.setBirthDay(Utils.createTimestamp(userForm.getBirthDay()));
            }
            if (StringUtils.hasLength(userForm.getPhone())) {
                userDo.setPhone(userForm.getPhone());
            }
            if (StringUtils.hasLength(userForm.getPhoto())) {
                userDo.setPhoto(imgPath);
            }
            return userDo;
        } catch (Exception e) {
            logger.error("用户注册失败", e);
            throw new ServiceException(e);
        }
    }

    private UserDo buildUserUpdate(UserForm userForm) throws ServiceException {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String username = getCurrentUsername();
            UserDo dbUserDo = userDao.getUserByName(username);
            UserDo userDo = BeanCopierUtil.create(dbUserDo, UserDo.class);
            UserDo userDo1 = userDao.getUserByName(username);

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
            if (StringUtils.hasLength(userForm.getPhoto())) {
                FileUtils.fileDelete(userDo1.getPhoto());
                FileUtils.fileWrite(userDo1.getPhoto(), userForm.getPhoto());
            }
            return userDo;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private UserRoleDo buildUserRoleSave(UserDo userDo) throws ServiceException {
        try {
            UserRoleDo userRoleDo = new UserRoleDo();
            RoleDo roleDo = roleDao.getRoleByName("user");
            userRoleDo.setRoleId(roleDo != null ? roleDo.getRoleId() : "-1");
            userRoleDo.setUserId(userDo.getUserId());
            return userRoleDo;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
