package com.example.tabpat.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
@Data
@ToString
public class FileUploadDo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 文件Id
     */
    private String fileId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件地址
     */
    private String file;
    /**
     * userId
     */
    private String userId;
}
