package com.example.tabpat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tabpat.domain.ArticlesCountDo;
import com.example.tabpat.domain.ArticlesDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.dto.ArticlesCountDto;
import com.example.tabpat.util.SystemClock;
import com.google.protobuf.ServiceException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.tabpat.code.HttpStatusCode.OK;

//此service可能冗余
@Service
public class ArticlesCountService extends BaseService {
    //点赞数统计
    @Transactional
    public Result list() throws ServiceException {
        try {
            UserDo userDo = userDao.getUserByName(getCurrentUsername());
            String userId = userDo.getUserId();
            QueryWrapper<ArticlesCountDo> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            List<ArticlesCountDo> articlesCountDoList = articlesCountDao.selectList(wrapper);
            List<ArticlesCountDto> articlesCountDtoList = new ArrayList<>();
            if (articlesCountDoList.isEmpty()) {
                return Result.success(200, "获取成功", articlesCountDoList);
            }
            for (ArticlesCountDo articlesCountDo : articlesCountDoList) {
                ArticlesCountDto articlesCountDto = listShowDto(articlesCountDo);
                articlesCountDtoList.add(articlesCountDto);
            }
            return Result.success(OK, "获取成功", articlesCountDtoList);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private ArticlesCountDto listShowDto(ArticlesCountDo articlesCountDo) {
        ArticlesCountDto articlesCountDto = new ArticlesCountDto();
        articlesCountDto.setDate(articlesCountDo.getDate());
        articlesCountDto.setLikeCount(articlesCountDo.getLikeCount());
        return articlesCountDto;
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
