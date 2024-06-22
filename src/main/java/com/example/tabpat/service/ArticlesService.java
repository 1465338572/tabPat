package com.example.tabpat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tabpat.dao.ArticlesLabelDao;
import com.example.tabpat.domain.ArticlesDo;
import com.example.tabpat.domain.ArticlesLabelDo;
import com.example.tabpat.domain.LabelDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.dto.ArticlesDto;
import com.example.tabpat.form.ArticlesForm;
import com.example.tabpat.form.ArticlesLabelForm;
import com.example.tabpat.query.ArticlesQuery;
import com.example.tabpat.util.BeanCopierUtil;
import com.example.tabpat.util.PrimaryKeyUtil;
import com.example.tabpat.util.Utils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.protobuf.ServiceException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticlesService extends BaseService {

    protected ArticlesLabelService articlesLabelService;

    @Autowired
    public void setArticlesLabelService(ArticlesLabelService articlesLabelService) {
        this.articlesLabelService = articlesLabelService;
    }

    //获取文章列表并分页
    @Transactional
    public Result list(ArticlesQuery articlesQuery) throws ServiceException {
        try {
            UserDo userDo = userDao.getUserByName(getCurrentUsername());
            String userId = userDo.getUserId();
            QueryWrapper<ArticlesDo> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            if (StringUtils.hasLength(articlesQuery.getArticlesTitle())) {
                wrapper.like("article_title", articlesQuery.getArticlesTitle());
            }
            wrapper.orderByDesc("article_date");
            //启动pagehelper
            PageHelper.startPage(articlesQuery.getPageNum(), articlesQuery.getPageSize());
            List<ArticlesDo> articlesDoList = articlesDao.selectList(wrapper);
            //使用源数据记录total
            PageInfo<ArticlesDo> articlesDoPageInfo = new PageInfo<>(articlesDoList);
            List<ArticlesDto> articlesDtoList = new ArrayList<>();
            if (articlesDoList.isEmpty()) {
                return Result.success(200, "获取成功", articlesDoList);
            }

            for (ArticlesDo articlesDo : articlesDoList) {
                ArticlesDto articlesDto = listShowDto(articlesDo, userId);
                articlesDtoList.add(articlesDto);
            }
            PageInfo<ArticlesDto> articlesDtoPageInfo = new PageInfo<>(articlesDtoList);
            //将源数据total赋值给dtoTotal
            articlesDtoPageInfo.setTotal(articlesDoPageInfo.getTotal());
            return Result.success(200, "获取成功", articlesDtoPageInfo);

        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private ArticlesDto listShowDto(ArticlesDo articlesDo, String userId) throws IOException {
        ArticlesDto articlesDto = new ArticlesDto();
        String articleId = articlesDo.getArticleId();
        ArticlesLabelDo articlesLabelDo = articlesLabelDao.getArticlesLabel(articleId, null);
        if (articlesLabelDo != null) {
            LabelDo labelDo = labelDao.getLabelByLabelId(userId, articlesLabelDo.getLabelId());
            articlesDto.setLabelName(labelDo.getLabelName());
        }
//        String content = FileUtils.fileRead(articlesDo.getArticleContent());
        articlesDto.setArticleId(articleId);
        articlesDto.setArticleTitle(articlesDo.getArticleTitle());
//        articlesDto.setArticleContent(content);
        articlesDto.setArticleView(articlesDo.getArticleView());
        articlesDto.setArticleDate(articlesDo.getArticleDate());
        articlesDto.setArticleLikeCount(articlesDo.getArticleLikeCount());
        articlesDto.setArticleShow(articlesDo.getArticleShow());
        return articlesDto;
    }

    //各标签下文章
    @Transactional
    public Result listWidthLabel(ArticlesQuery articlesQuery, String labelId) throws ServiceException {
        try {
            List<ArticlesLabelDo> ArticlesLabelDoLists = articlesLabelDao.selectArticlesLabel(labelId);
            UserDo userDo = userDao.getUserByName(getCurrentUsername());
            String userId = userDo.getUserId();
            QueryWrapper<ArticlesDo> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            if (StringUtils.hasLength(articlesQuery.getArticlesTitle())) {
                wrapper.like("article_title", articlesQuery.getArticlesTitle());
            }
            wrapper.orderByDesc("article_date");
            List<String> articlesLabelIds = new ArrayList<>();
            for (ArticlesLabelDo articlesLabelDo : ArticlesLabelDoLists) {
                articlesLabelIds.add(articlesLabelDo.getArticlesId());
            }
            //防止无数据报错的临时解决办法 --事实上根本毫无头绪，可能本身逻辑就有问题，但修改颇为麻烦
            if (!articlesLabelIds.isEmpty()) {
                wrapper.in("article_id", articlesLabelIds);
            } else {
                wrapper.in("article_id", articlesLabelIds.add("0"));
            }

            //启动pagehelper
            PageHelper.startPage(articlesQuery.getPageNum(), articlesQuery.getPageSize());
            List<ArticlesDo> articlesDoList = articlesDao.selectList(wrapper);
            //使用源数据记录total
            PageInfo<ArticlesDo> articlesDoPageInfo = new PageInfo<>(articlesDoList);
            List<ArticlesDto> articlesDtoList = new ArrayList<>();
            if (articlesDoList.isEmpty()) {
                return Result.success(200, "获取成功", articlesDoList);
            }

            for (ArticlesDo articlesDo : articlesDoList) {
                ArticlesDto articlesDto = listShowDto(articlesDo, userId);
                articlesDtoList.add(articlesDto);
            }
            PageInfo<ArticlesDto> articlesDtoPageInfo = new PageInfo<>(articlesDtoList);
            //将源数据total赋值给dtoTotal
            articlesDtoPageInfo.setTotal(articlesDoPageInfo.getTotal());
            return Result.success(200, "获取成功", articlesDtoPageInfo);

        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    public Result get(String articleId) throws ServiceException {
        try {
            ArticlesDo articlesDo = articlesDao.getArticlesByArticleId(articleId);
            ArticlesDto articlesDto = getShowDto(articlesDo);
            return Result.success(200, "获取成功", articlesDto);

        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private ArticlesDto getShowDto(ArticlesDo articlesDo) throws IOException {
        ArticlesDto articlesDto = new ArticlesDto();

        String content = FileUtils.fileRead(articlesDo.getArticleContent());
        String img = FileUtils.fileRead(articlesDo.getArticleImg());

        articlesDto.setArticleId(articlesDo.getArticleId());
        articlesDto.setArticleTitle(articlesDo.getArticleTitle());
        articlesDto.setArticleContent(content);
        articlesDto.setArticleView(articlesDo.getArticleView());
        articlesDto.setArticleDate(articlesDo.getArticleDate());
        articlesDto.setArticleLikeCount(articlesDo.getArticleLikeCount());
        articlesDto.setArticleShow(articlesDo.getArticleShow());
        articlesDto.setArticleImg(img);
        return articlesDto;
    }

    //文章保存
    @Transactional
    public Result save(ArticlesForm articlesForm) throws ServiceException {
        try {
            //用户获取
            UserDo userDo = userDao.getUserByName(getCurrentUsername());
            String userId = userDo.getUserId();

            File directory = new File("");
            String dirPath = directory.getCanonicalPath() + "\\" + userId;
            FileUtils.mkdir(dirPath);
            ArticlesDo articlesDo = buildArticlesSave(articlesForm, userId, dirPath);
            articlesDao.insert(articlesDo);
            //标签博客管理
            ArticlesLabelForm articlesLabelForm = buildALForm(articlesForm, articlesDo);
            articlesLabelService.save(articlesLabelForm);
            return Result.success(200, "博客已保存", articlesDo.getArticleId());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private ArticlesLabelForm buildALForm(ArticlesForm articlesForm, ArticlesDo articlesDo) {
        ArticlesLabelForm articlesLabelForm = new ArticlesLabelForm();
        articlesLabelForm.setArticlesId(articlesDo.getArticleId());
        articlesLabelForm.setLabelId(articlesForm.getLabelId());
        return articlesLabelForm;
    }

    private ArticlesDo buildArticlesSave(ArticlesForm articlesForm, String userId, String dirPath) throws ServiceException {
        try {

            //随机Id创造
            String articleId = PrimaryKeyUtil.get();

            //文本文件创造
            String filPath = dirPath + "\\" + System.currentTimeMillis() + "txt.txt";
            String imgPath = dirPath + "\\" + System.currentTimeMillis() + "img.txt";
            FileUtils.fileWrite(filPath, articlesForm.getArticleContent());
            FileUtils.fileWrite(imgPath, articlesForm.getArticleImg());

            ArticlesDo articlesDo = BeanCopierUtil.create(articlesForm, ArticlesDo.class);

            articlesDo.setArticleId(articleId);
            articlesDo.setArticleTitle(articlesForm.getArticleTitle());
            articlesDo.setArticleContent(filPath);
            articlesDo.setArticleView(0);
            articlesDo.setArticleLikeCount(0);
            articlesDo.setArticleDate(System.currentTimeMillis());
            articlesDo.setUserId(userId);
            articlesDo.setArticleShow(articlesForm.getArticleShow());
            articlesDo.setArticleImg(imgPath);
            return articlesDo;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    //文章编辑更新
    @Transactional
    public Result update(ArticlesForm articlesForm) throws ServiceException {
        try {
            ArticlesDo articlesDo = buildArticlesUpdate(articlesForm);
            articlesDao.updateById(articlesDo);
            return Result.success(200, "博客已保存", articlesDo.getArticleId());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private ArticlesDo buildArticlesUpdate(ArticlesForm articlesForm) throws Exception {
        try {
            ArticlesDo articlesDo = BeanCopierUtil.create(articlesForm, ArticlesDo.class);
            ArticlesDo articlesDo1 = articlesDao.getArticlesByArticleId(articlesForm.getArticleId());
            if (StringUtils.hasLength(articlesForm.getArticleId())) {
                articlesDo.setArticleId(articlesForm.getArticleId());
            }
            if (StringUtils.hasLength(articlesForm.getArticleTitle())) {
                articlesDo.setArticleTitle(articlesForm.getArticleTitle());
            }
            if (StringUtils.hasLength(articlesForm.getArticleContent())) {
                FileUtils.fileDelete(articlesDo1.getArticleContent());
                FileUtils.fileWrite(articlesDo1.getArticleContent(), articlesForm.getArticleContent());
                articlesDo.setArticleContent(articlesDo1.getArticleContent());
            }
            if (StringUtils.hasLength(articlesForm.getArticleImg())) {
                FileUtils.fileDelete(articlesDo1.getArticleImg());
                FileUtils.fileWrite(articlesDo1.getArticleImg(), articlesForm.getArticleImg());
                articlesDo.setArticleImg(articlesDo1.getArticleImg());
            }
            if (articlesForm.getArticleShow() != null) {
                articlesDo.setArticleShow(articlesForm.getArticleShow());
            }
            articlesDo.setArticleDate(System.currentTimeMillis());
            return articlesDo;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    //文章删除
    @Transactional
    public Result delete(ArticlesForm articlesForm) throws ServiceException {
        try {
            List<String> articleIds = articlesForm.getArticleIds();

            ArticlesLabelForm articlesLabelForm = new ArticlesLabelForm();
            articlesLabelForm.setArticlesIds(articleIds);
            articlesLabelService.delete(articlesLabelForm);

            for (String articleId : articleIds) {
                ArticlesDo articlesDo = articlesDao.getArticlesByArticleId(articleId);
                articlesDao.deleteById(articleId);
                FileUtils.fileDelete(articlesDo.getArticleContent());
                FileUtils.fileDelete(articlesDo.getArticleImg());
            }

            return Result.success(200, "博客删除成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    //每日文章统计
    @Transactional
    public Result articlesTimeCount(String year) throws ServiceException {
        try {
            UserDo userDo = userDao.getUserByName(getCurrentUsername());
            String userId = userDo.getUserId();
            QueryWrapper<ArticlesDo> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            wrapper.orderByDesc("article_date");
            List<ArticlesDo> articlesDoList = articlesDao.selectList(wrapper);
            if (articlesDoList.isEmpty()) {
                return Result.success(200, "获取成功", articlesDoList);
            }
            ArrayList<String> timeList = new ArrayList<>();
            for (ArticlesDo articlesDo : articlesDoList) {
                if (year != null && !year.isEmpty()) {
                    if (Utils.timestampToTime(articlesDo.getArticleDate(), "year").equals(year)) {
                        String time = Utils.timestampToTime(articlesDo.getArticleDate(), "day");
                        timeList.add(time);
                    }
                }
            }

            List<List<Object>> articlesTimeCount = getArticlesTimeCount(timeList);

            return Result.success(200, "查询成功", articlesTimeCount);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    //计数
    private static List<List<Object>> getArticlesTimeCount(ArrayList<String> timeList) {
        if (timeList == null) {
            return null;
        }
        Map<String, Integer> dateCountMap = new HashMap<>();
        for (String time : timeList) {
            dateCountMap.put(time, dateCountMap.getOrDefault(time, 0) + 1);
        }

        List<List<Object>> articlesTimeCount = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : dateCountMap.entrySet()) {
            List<Object> dateCountPair = new ArrayList<>();
            dateCountPair.add(entry.getKey());
            dateCountPair.add(entry.getValue());
            articlesTimeCount.add(dateCountPair);
        }
        return articlesTimeCount;
    }

}
