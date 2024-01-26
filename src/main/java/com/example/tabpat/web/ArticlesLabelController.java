package com.example.tabpat.web;

import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.ArticlesLabelForm;
import com.example.tabpat.form.LabelForm;
import com.example.tabpat.service.ArticlesLabelService;
import com.example.tabpat.service.Result;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticlesLabelController {

    private ArticlesLabelService articlesLabelService;

    @Autowired
    public void setArticlesLabelService(ArticlesLabelService articlesLabelService){ this.articlesLabelService = articlesLabelService; }

    @PostMapping(value ="/secure/saveAL" )
    @ResponseBody
    public Result save(@RequestBody ArticlesLabelForm articlesLabelForm, HttpServletResponse response) {
        Result result;
        try{
            result = articlesLabelService.save(articlesLabelForm);

            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        }catch (Exception e){
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @PutMapping(value ="/secure/updateAL" )
    @ResponseBody
    public Result update(@RequestBody ArticlesLabelForm articlesLabelForm, HttpServletResponse response) throws Exception {
        Result result;
        try{
            result = articlesLabelService.update(articlesLabelForm);

            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        }catch (ServiceException e){
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @DeleteMapping(value = "/secure/deleteAL")
    @ResponseBody
    public Result delete(@RequestBody ArticlesLabelForm articlesLabelForm, HttpServletResponse response) throws ServiceException {
        Result result;
        try{
            result = articlesLabelService.delete(articlesLabelForm);

            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        }catch (ServiceException e){
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());

        }
        return result;
    }
}
