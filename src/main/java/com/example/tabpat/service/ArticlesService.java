package com.example.tabpat.service;

import com.example.tabpat.domain.ArticlesDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.form.ArticlesForm;
import com.example.tabpat.util.BeanCopierUtil;
import com.example.tabpat.util.PrimaryKeyUtil;
import com.google.protobuf.ServiceException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;

import static com.mysql.cj.protocol.a.MysqlTextValueDecoder.getTime;

@Service
public class ArticlesService extends BaseService {
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
            return Result.success(200, "文章发布成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private ArticlesDo buildArticlesSave(ArticlesForm articlesForm, String userId, String dirPath) throws ServiceException {
        try {

            //随机Id创造
            String articleId = PrimaryKeyUtil.get();

            //文本文件创造
            String filPath = dirPath + "\\" + System.currentTimeMillis() + "txt.txt";
            FileUtils.fileWrite(filPath, articlesForm.getArticleContent());

            ArticlesDo articlesDo = BeanCopierUtil.create(articlesForm, ArticlesDo.class);

            articlesDo.setArticleId(articleId);
            articlesDo.setArticleTitle(articlesForm.getArticleTitle());
            articlesDo.setArticleContent(filPath);
            articlesDo.setArticleView(0);
            articlesDo.setArticleLikeCount(0);
            articlesDo.setArticleDate(System.currentTimeMillis());
            articlesDo.setUserId(userId);
            articlesDo.setArticleShow(articlesForm.getArticleShow());
            return articlesDo;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
