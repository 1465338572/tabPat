package com.example.tabpat.web;

import com.example.tabpat.annotation.ArticlesView;
import com.example.tabpat.annotation.UnderlineToCamel;
import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.ArticlesForm;
import com.example.tabpat.query.ArticlesQuery;
import com.example.tabpat.service.ArticlesService;
import com.example.tabpat.service.Result;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticlesController {

    private ArticlesService articlesService;

    @Autowired
    public void setArticlesService(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    /**
     * 获取所有博客文章
     *
     * @param articlesQuery
     * @param response
     * @return
     * @throws ServiceException
     */
    @GetMapping(value = "/public/pubList")
    @ResponseBody
    public Result pubList(@UnderlineToCamel ArticlesQuery articlesQuery, HttpServletResponse response) {
        Result result;
        try {
            result = articlesService.pubList(articlesQuery);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());

        }
        return result;
    }

    /**
     * 获取单个博客
     *
     * @param articleId 博客id
     * @param response
     * @return
     */
    @GetMapping(value = "/public/getArticle/{article_id}")
    @ResponseBody
    @ArticlesView
    public Result get(@PathVariable("article_id") String articleId, HttpServletResponse response) {
        Result result;
        try {
            result = articlesService.get(articleId);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }


    /**
     * 点赞
     */
    @PutMapping(value = "/public/likeArticle/{article_id}")
    @ResponseBody
    public Result like(@PathVariable("article_id") String articleId, HttpServletResponse response) {
        Result result;
        try {
            result = articlesService.like(articleId);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }


    /**
     * 获取单个用户下的所有文章
     *
     * @param articlesQuery
     * @param response
     * @return
     */
    @GetMapping(value = "/secure/listArticle")
    @ResponseBody
    public Result list(@UnderlineToCamel ArticlesQuery articlesQuery, HttpServletResponse response) {
        Result result;
        try {
            result = articlesService.list(articlesQuery);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    /**
     * 获取文章标签
     *
     * @param articlesQuery
     * @param labelId
     * @param response
     * @return
     */
    @GetMapping(value = "/secure/listWidthLabel/{label_id}")
    @ResponseBody
    public Result listWidthLabel(@UnderlineToCamel ArticlesQuery articlesQuery, @PathVariable("label_id") String labelId, HttpServletResponse response) {
        Result result;
        try {
            result = articlesService.listWidthLabel(articlesQuery, labelId);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    /**
     * 文章保存
     *
     * @param articlesForm
     * @param response
     * @return
     */
    @PostMapping(value = "/secure/addArticle")
    @ResponseBody
    public Result save(@RequestBody ArticlesForm articlesForm, HttpServletResponse response) {
        Result result;
        try {
            result = articlesService.save(articlesForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    /**
     * 文章更新
     *
     * @param articlesForm
     * @param response
     * @return
     */
    @PutMapping(value = "/secure/updateArticle")
    @ResponseBody
    public Result update(@RequestBody ArticlesForm articlesForm, HttpServletResponse response) {
        Result result;
        try {
            result = articlesService.update(articlesForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    /**
     * 文章删除
     *
     * @param articlesForm
     * @param response
     * @return
     * @throws ServiceException
     */
    @DeleteMapping(value = "/secure/deleteArticle")
    @ResponseBody
    public Result delete(@RequestBody ArticlesForm articlesForm, HttpServletResponse response) throws ServiceException {
        Result result;
        try {
            result = articlesService.delete(articlesForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    /**
     * 每日文章统计
     *
     * @param year
     * @param response
     * @return
     */
    @GetMapping(value = "/secure/getArticlesTimeCount")
    @ResponseBody
    public Result articlesTimeCount(@UnderlineToCamel String year, HttpServletResponse response) {
        Result result;
        try {
            result = articlesService.articlesTimeCount(year);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }
}
