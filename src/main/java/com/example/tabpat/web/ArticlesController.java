package com.example.tabpat.web;

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

    @GetMapping(value = "/secure/listWidthLabel/{label_id}")
    @ResponseBody
    public Result listWidthLabel(@UnderlineToCamel ArticlesQuery articlesQuery,@PathVariable("label_id") String labelId, HttpServletResponse response) {
        Result result;
        try {
            result = articlesService.listWidthLabel(articlesQuery,labelId);
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

    @GetMapping(value = "/secure/getArticle/{article_id}")
    @ResponseBody
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

}
