package com.example.tabpat.web;

import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.ArticlesForm;
import com.example.tabpat.service.ArticlesService;
import com.example.tabpat.service.Result;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticlesController {

    private ArticlesService articlesService;

    @Autowired
    public void setArticlesService(ArticlesService articlesService) {
        this.articlesService = articlesService;
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

}
