package com.example.tabpat.web;

import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.service.AppService;
import com.example.tabpat.service.Result;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppController {
    private AppService appService;

    @Autowired
    public void setAppService(AppService appService){
        this.appService = appService;
    }

    @GetMapping(value = "/secure/apps")
    @ResponseBody
    public Result appsList(HttpServletResponse response) {
        Result result;
        try {
            result = appService.list();
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
}
