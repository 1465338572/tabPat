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
}
