package com.example.tabpat.form;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件上传
 */
@Data
@ToString
public class FileUploadForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String fileHash;
    //完整的文件名
    private String fileName;
    //文件
    private MultipartFile file;
    //当前文件分片索引
    private int chunkIndex;
    //文件分片总数
    private int totalChunks;
}
