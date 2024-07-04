package com.example.tabpat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tabpat.domain.FileUploadDo;
import org.apache.ibatis.annotations.Param;

public interface FileUploadDao extends BaseMapper<FileUploadDo> {
    FileUploadDo selectByFileId(@Param("fileId") String fileId);
}
