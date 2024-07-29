package com.example.tabpat.config;

import com.example.tabpat.util.IpUtils;
import com.example.tabpat.util.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * AOP切面控制
 */
@Aspect
@Log4j2
@Configuration
public class ArticlesViewAspect {
    /**
     * 引入redis工具类
     */
    private RedisUtils redisUtils;

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 获取当前的ServletRequest
     *
     * @return
     */
    protected HttpServletRequest servletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.example.tabpat.annotation.ArticlesView)")
    public void articlesViewPointCut() {
    }

    /**
     * 切入处理，环绕通知
     */
    @Around("articlesViewPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object articlesId = args[0];
        Object obj = null;
        try {
            //获取请求的ip
            String ipAddr = IpUtils.getIpAddress(servletRequest());
            log.info(ipAddr);
            //设置存入的key
            String key = "ARTICLES_ID:" + articlesId;
            // 将存入到缓存中
            System.out.println(ipAddr+"这是ip");
            Long count = this.redisUtils.hyAdd(key, ipAddr);
            if (count == 0) {
                log.info("{},已经访问过了", ipAddr);
            }
            obj = joinPoint.proceed();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return obj;
    }
}
