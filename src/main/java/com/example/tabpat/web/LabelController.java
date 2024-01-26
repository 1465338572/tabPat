package com.example.tabpat.web;

import com.example.tabpat.annotation.UnderlineToCamel;
import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.LabelForm;
import com.example.tabpat.form.UserForm;
import com.example.tabpat.query.ArticlesQuery;
import com.example.tabpat.query.LabelQuery;
import com.example.tabpat.service.LabelService;
import com.example.tabpat.service.Result;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class LabelController {


    private LabelService labelService;
    @Autowired
    public void setLabelService(LabelService labelService){
        this.labelService = labelService;
    }

    @GetMapping(value = "/secure/listLabel")
    @ResponseBody
    public Result list(@UnderlineToCamel LabelQuery labelQuery, HttpServletResponse response) {
        Result result;
        try {
            result = labelService.list(labelQuery);
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

    @GetMapping(value = "/secure/getLabel/{label_name}")
    @ResponseBody
    public Result get(@PathVariable("label_name") String labelName, HttpServletResponse response) {
        Result result;
        try {
            result = labelService.get(labelName);
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

    @PostMapping(value = "/secure/addLabel")
    @ResponseBody
    public Result save(@RequestBody LabelForm labelForm, HttpServletResponse response) {
        Result result;
        try {
            result =labelService.save(labelForm);
            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @PutMapping(value = "/secure/updateLabel")
    @ResponseBody
    public Result update(@RequestBody LabelForm labelForm, HttpServletResponse response) {
        Result result;
        try {
            result =labelService.update(labelForm);
            if (result.getCode() != 200){
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @DeleteMapping(value = "/secure/deleteLabel")
    @ResponseBody
    public Result delete(@RequestBody LabelForm labelForm, HttpServletResponse response) throws ServiceException {
        Result result;
        try{
            result = labelService.delete(labelForm);

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
