package com.example.tabpat.web;

import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.FileUploadForm;
import com.example.tabpat.service.FileUploadService;
import com.example.tabpat.service.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FileUploadController {
    private static final Logger logger = LogManager.getLogger(FileUploadService.class);

    private FileUploadService fileUploadService;

    @Autowired
    public void setFileUploadService(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/secure/uploadFile")
    @ResponseBody
    public Result fileUpload(FileUploadForm fileUploadForm, HttpServletResponse response) {
        Result result;
        try {
            result = fileUploadService.uploadChunk(fileUploadForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (Exception e) {
            logger.error("文件上传失败",e);
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    @GetMapping(value = "/secure/download/{file_id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable("file_id") String fileId, HttpServletResponse response) {
        try {
            return fileUploadService.fileDownload(fileId, response);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
