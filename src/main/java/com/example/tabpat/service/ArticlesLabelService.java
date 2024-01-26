package com.example.tabpat.service;

import com.example.tabpat.domain.ArticlesDo;
import com.example.tabpat.domain.ArticlesLabelDo;
import com.example.tabpat.form.ArticlesForm;
import com.example.tabpat.form.ArticlesLabelForm;
import com.example.tabpat.util.BeanCopierUtil;
import com.google.protobuf.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.tabpat.code.HttpStatusCode.OK;


@Service
public class ArticlesLabelService extends BaseService{

    @Transactional
    public Result save(ArticlesLabelForm articlesLabelForm) throws ServiceException {
        try {

            ArticlesLabelDo articlesLabelDo = buildArticlesLabelSave(articlesLabelForm);
            articlesLabelDao.insert(articlesLabelDo);
            return Result.success(200, "标签添加成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private ArticlesLabelDo buildArticlesLabelSave(ArticlesLabelForm articlesLabelForm){
        ArticlesLabelDo articlesLabelDo = BeanCopierUtil.create(articlesLabelForm,ArticlesLabelDo.class);
        articlesLabelDo.setArticlesId(articlesLabelForm.getArticlesId());
        articlesLabelDo.setLabelId(articlesLabelForm.getLabelId());
        return articlesLabelDo;
    }

    @Transactional
    public Result update(ArticlesLabelForm articlesLabelForm) throws ServiceException {
        try {
            ArticlesLabelDo articlesLabelDo = buildArticlesLabelUpdate(articlesLabelForm);
            articlesLabelDao.updateById(articlesLabelDo);
            return Result.success(200, "便签更新成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    public ArticlesLabelDo buildArticlesLabelUpdate(ArticlesLabelForm articlesLabelForm){
        ArticlesLabelDo articlesLabelDo = BeanCopierUtil.create(articlesLabelForm,ArticlesLabelDo.class);
        articlesLabelDo.setArticlesId(articlesLabelForm.getArticlesId());
        articlesLabelDo.setLabelId(articlesLabelForm.getLabelId());
        return articlesLabelDo;
    }
    @Transactional
    public Integer getCount(ArticlesLabelForm articlesLabelForm) throws ServiceException {

        try {
            return articlesLabelDao.getCountById(articlesLabelForm.getLabelId());
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }


    @Transactional
    public Result delete(ArticlesLabelForm articlesLabelForm) throws ServiceException{
        try{
            List<String> articlesIds = articlesLabelForm.getArticlesIds();
            for (String articlesId : articlesIds){
                ArticlesLabelDo articlesLabelDo = articlesLabelDao.getArticlesLabel(articlesId,null);
                articlesLabelDao.deleteById(articlesLabelDo);
            }
            return Result.success(OK, "删除成功");
        }catch(Exception e){
            throw new ServiceException(e);
        }
    }
}
