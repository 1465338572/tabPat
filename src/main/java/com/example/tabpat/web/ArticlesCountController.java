package com.example.tabpat.web;

import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.service.ArticlesCountService;
import com.example.tabpat.service.Result;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticlesCountController {
    private ArticlesCountService articlesCountService;

    @Autowired
    public void setArticlesCountService(ArticlesCountService articlesCountService) {
        this.articlesCountService = articlesCountService;
    }

    @GetMapping(value = "/secure/listArticlesCount")
    @ResponseBody
    public Result list(HttpServletResponse response) {
        Result result;
        try {
            result = articlesCountService.list();
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
