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
        try {
            return appService.list();
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            return Result.create(HttpStatusCode.SERVICEERROR, e.getMessage());        }
    }
}
