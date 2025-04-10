package com.example.tabpat.service;

import com.example.tabpat.config.JwtConfig;
import com.example.tabpat.domain.RoleDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.domain.UserRoleDo;
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
        map.put("username", loginForm.getUsername());
        map.put("roles", roles);
        return map;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDo user = userDao.getUserByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<UserRoleDo> userRoleDoList = userRoleDao.selectByUserId(user.getUserId());

        // 这里使用 Set 避免角色重复
        Set<String> roleNames = new HashSet<>();
        for (UserRoleDo userRoleDo : userRoleDoList) {
            List<RoleDo> roles = roleDao.getRoleById(userRoleDo.getRoleId());
            for (RoleDo role : roles) {
                // 只关心角色名，避免重复
                roleNames.add(role.getRoleName());
            }
        }
        // 转换角色名为 GrantedAuthority 对象
        List<SimpleGrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 返回包含角色权限的 UserDetails
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

}
