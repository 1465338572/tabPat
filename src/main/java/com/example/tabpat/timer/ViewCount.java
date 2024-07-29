package com.example.tabpat.timer;

import com.example.tabpat.dao.ArticlesDao;
import com.example.tabpat.domain.ArticlesDo;
import com.example.tabpat.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class ViewCount {
    private RedisUtils redisUtils;
    private ArticlesDao articlesDao;

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Autowired
    public void setArticlesDao(ArticlesDao articlesDao) {
        this.articlesDao = articlesDao;
    }

    /**
     * 定时更新问题浏览量到数据库中
     * 每天凌晨两点跑一次
     */
    @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(cron = "0/5 0/1 * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void updateArticlesView() {
        // 获取全部的key
        String pattern = "Articles_ID:*";
        Set<String> keys = redisUtils.keys(pattern);
        try {
            for (String key : keys) {
                Long viewCount = redisUtils.hySize(key);
                // 将key拆分
                String[] split = key.split(":");
                // 根据问题id获取
                ArticlesDo articlesDo = articlesDao.getArticlesByArticleId(split[1]);
                if (articlesDo == null) {
                    throw new Exception("问题不存在");
                }
                // 更改浏览量
                articlesDo.setArticleView(viewCount.intValue() + articlesDo.getArticleView());
                int count = articlesDao.updateById(articlesDo);
                if (count == 0) {
                    throw new Exception("问题浏览量更新失败");
                }
                // 删除key
                redisUtils.delete(key);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

}
