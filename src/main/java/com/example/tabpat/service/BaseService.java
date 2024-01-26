package com.example.tabpat.service;

import com.example.tabpat.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class BaseService {
    protected RoleDao roleDao;
    protected UserDao userDao;
    protected UserRoleDao userRoleDao;

    protected UserThreadDao userThreadDao;

    protected ApiKeyDao apiKeyDao;

    protected AppDao appDao;

    protected ArticlesDao articlesDao;

    protected LabelDao labelDao;

    protected UserLabelDao userLabelDao;

    protected ArticlesLabelDao articlesLabelDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setUserRoleDao(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    @Autowired
    public void setUserThreadlDao(UserThreadDao userThreadDao) {
        this.userThreadDao = userThreadDao;
    }

    @Autowired
    public void setApiKeyDao(ApiKeyDao apiKeyDao) {
        this.apiKeyDao = apiKeyDao;
    }

    @Autowired
    public void setAppDao(AppDao appDao) {
        this.appDao = appDao;
    }

    @Autowired
    public void setArticlesDao(ArticlesDao articlesDao) {
        this.articlesDao = articlesDao;
    }

    @Autowired
    public void setLabelDao(LabelDao labelDao) {
        this.labelDao = labelDao;
    }

    @Autowired
    public void setUserLabelDao(UserLabelDao userLabelDao){
        this.userLabelDao = userLabelDao;
    }

    @Autowired
    public void setArticlesLabelDao(ArticlesLabelDao articlesLabelDao){ this.articlesLabelDao = articlesLabelDao; }

    //邮箱检测方法
    public static final String REGEX_EMAIL = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    public boolean isVaildEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 正则表达式：手机号验证<br>
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 <br>
     * 此方法中前三位格式有： <br>
     * 13+任意数 <br>
     * 15，19+除4的任意数<br>
     * 18+除1和4的任意数 <br>
     * 17+除9的任意数 <br>
     * 147
     */
    public static final String REGEX_MOBILE = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

    public boolean isValidMobileNo(String mobileNo) {
        return Pattern.matches(REGEX_MOBILE, mobileNo);
    }

    //获取当前登录用户的用户名
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // 用户未认证
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }

        return null; // 无法获取用户名
    }
}
