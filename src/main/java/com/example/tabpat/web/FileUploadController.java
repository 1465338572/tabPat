package com.example.tabpat.web;

import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.FileUploadForm;
import com.example.tabpat.service.FileUploadService;
import com.example.tabpat.service.Result;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {
    private FileUploadService fileUploadService;

    @Autowired
    public void setFileUploadService(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/secure/uploadFile")
    @ResponseBody
    public Result fileUpload(FileUploadForm fileUploadForm, HttpServletResponse response) throws Exception {
        Result result;
        try {
            result = fileUploadService.uploadChunk(fileUploadForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (Exception e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

}
