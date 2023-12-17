package com.example.tabpat.service;

import com.example.tabpat.config.JwtConfig;
import com.example.tabpat.domain.RoleDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.domain.UserRoleDo;
import com.example.tabpat.domain.UserThreadDo;
import com.example.tabpat.form.LoginForm;
import com.example.tabpat.util.JwtTokenUtil;
import com.example.tabpat.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService extends BaseService implements UserDetailsService {

    private JwtTokenUtil jwtTokenUtil;
    private JwtConfig jwtConfig;
    private RedisUtils redisUtils;

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setJwtConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 根据用户名查询用户
     */


    public Map login(LoginForm loginForm) throws RuntimeException {
        UserDo dbUser = userDao.getUserByName(loginForm.getUsername());
        if (dbUser == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean passwordMatch = encoder.matches(loginForm.getPassword(), dbUser.getPassword());
        //密码错误
        if (!passwordMatch) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        // 用户名 密码匹配，获取用户详细信息（包含角色Role）
        final UserDetails userDetails = this.loadUserByUsername(loginForm.getUsername());

        // 根据用户详细信息生成token
        final String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, Object> map = getStringObjectMap(loginForm, userDetails, token);
        //将token存入redis(TOKEN_username, Bearer + token, jwt存放五天 过期时间) jwtConfig.time 单位[s]
        redisUtils.setEx(JwtConfig.REDIS_TOKEN_KEY_PREFIX + loginForm.getUsername(), jwtConfig.getPrefix() + token, jwtConfig.getTime(), TimeUnit.SECONDS);
        return map;
    }

    private Map<String, Object> getStringObjectMap(LoginForm loginForm, UserDetails userDetails, String token) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) { // SimpleGrantedAuthority是GrantedAuthority实现类
            // GrantedAuthority包含类型为String的获取权限的getAuthority()方法
            // 提取角色并放入List中
            roles.add(authority.getAuthority());
        }

        Map<String, Object> map = new HashMap<>(3);

        map.put("token", jwtConfig.getPrefix() + token);
        map.put("name", loginForm.getUsername());
        map.put("roles", roles);
        return map;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDo user = userDao.getUserByName(username);
        UserRoleDo userRoleDo = userRoleDao.selectById(user.getUserId());
        // 新建权限集合，SimpleGrantedAuthority是GrantedAuthority实现类
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(1);
        //用于添加用户的权限。将用户权限添加到authorities
        List<RoleDo> roles = roleDao.getRoleById(userRoleDo.getRoleId()); // 查询该用户的角色
        for (RoleDo role : roles) {
            // 将role的name放入权限的集合
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

}
