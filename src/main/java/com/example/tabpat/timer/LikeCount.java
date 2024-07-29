package com.example.tabpat.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tabpat.dao.ArticlesCountDao;
import com.example.tabpat.dao.ArticlesDao;
import com.example.tabpat.dao.UserDao;
import com.example.tabpat.domain.ArticlesCountDo;
import com.example.tabpat.domain.ArticlesDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.util.SystemClock;
import com.google.protobuf.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class LikeCount {
    private UserDao userDao;

    private ArticlesCountDao articlesCountDao;

    private ArticlesDao articlesDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setArticlesCountDao(ArticlesCountDao articlesCountDao) {
        this.articlesCountDao = articlesCountDao;
    }

    @Autowired
    public void setArticlesDao(ArticlesDao articlesDao) {
        this.articlesDao = articlesDao;
    }

    //每日统计点赞数量
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void save() throws ServiceException {
        try {
            //用户获取
            List<UserDo> userDoList = userDao.getUserList();
            for (UserDo userDo : userDoList) {
                String userId = userDo.getUserId();
                ArticlesCountDo lastArticlesCountDo = articlesCountDao.getLast(userId);
                Integer lastArticlesCount = 0;
                if (lastArticlesCountDo != null) {
                    lastArticlesCount = lastArticlesCountDo.getLikeCount();
                }
                ArticlesCountDo newArticlesCountDo = buildArticlesCountSave(lastArticlesCount, userId);
                articlesCountDao.insert(newArticlesCountDo);
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private ArticlesCountDo buildArticlesCountSave(Integer lastArticlesCount, String userId) {
        ArticlesCountDo articlesCountDo = new ArticlesCountDo();
        QueryWrapper<ArticlesDo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<ArticlesDo> articlesDoList = articlesDao.selectList(wrapper);
        Integer likeCount = 0;
        for (ArticlesDo articlesDo : articlesDoList) {
            likeCount += articlesDo.getArticleLikeCount();
        }
        likeCount -= lastArticlesCount;

        articlesCountDo.setLikeCount(likeCount);
        articlesCountDo.setDate(SystemClock.now());
        articlesCountDo.setUserId(userId);
        return articlesCountDo;
    }

}
