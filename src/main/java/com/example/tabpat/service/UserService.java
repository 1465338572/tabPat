package com.example.tabpat.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.tabpat.check.UserCheck;
import com.example.tabpat.dao.ApiKeyDao;
import com.example.tabpat.domain.ApiKeyDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.domain.UserRoleDo;
import com.example.tabpat.domain.UserThreadDo;
import com.example.tabpat.dto.UserDto;
import com.example.tabpat.form.UserForm;
import com.example.tabpat.query.UserQuery;
import com.example.tabpat.util.BeanCopierUtil;
import com.example.tabpat.util.ClientUtil;
import com.example.tabpat.util.PrimaryKeyUtil;
import com.example.tabpat.util.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
public class UserService extends BaseService {
    private UserCheck userCheck;

    private ClientUtil clientUtil;

    @Autowired
    public void setUserCheck(UserCheck userCheck) {
        this.userCheck = userCheck;
    }
    @Autowired
    public void setClientUtil(ClientUtil clientUtil){
        this.clientUtil = clientUtil;
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
            return Result.success(200, "用户创建成功",userDto);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
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
            UserThreadDo userThreadDo = getThread(userForm);
            userThreadDao.insert(userThreadDo);
            return Result.success(200, "用户创建成功");
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
            return Result.success(200, "用户更新成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private UserDto buildUserGet(UserDo userDo){
        return BeanCopierUtil.create(userDo, UserDto.class);
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
            if (StringUtils.hasLength(userForm.getQq())){
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
                userDo.setPhoto(userForm.getPhoto());
            }
            return userDo;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private UserThreadDo getThread(UserForm userForm) throws ServiceException {
        try {

            UserDo userDo = userDao.getUserByName(userForm.getUsername());
            UserThreadDo userThreadDo = new UserThreadDo();
            userThreadDo.setUserId(userDo.getUserId());
            userThreadDo.setRole(userDo.getName());

            JSONObject data = new JSONObject();
            JSONObject roleMessage = new JSONObject();
            roleMessage.put("role", "user");
            roleMessage.put("content", "");

            JSONArray arrayMessage = new JSONArray();
            arrayMessage.add(roleMessage);
            data.put("messages", arrayMessage);

            String response = clientUtil.doPost("https://api.openai.com/v1/threads",data);

            if (response != null) {
                // 处理响应
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response);
                String thread = rootNode.path("id").asText();
                userThreadDo.setThread(thread);
            }

            return userThreadDo;
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
            if (StringUtils.hasLength(userForm.getPhoto())) {
                userDo.setPhoto(userForm.getPhoto());
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
