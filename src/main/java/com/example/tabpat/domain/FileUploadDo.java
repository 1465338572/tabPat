package com.example.tabpat.domain;

import java.io.Serial;
import java.io.Serializable;

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
     * 文件分片总数
     */
    private Integer fileTotalChunks;
    /**
     * 当前文件存储分片数
     */
    private Integer fileChunkIndex;
    /**
     * userId
     */
    private String userId;
}
