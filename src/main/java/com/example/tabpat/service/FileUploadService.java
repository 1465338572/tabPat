package com.example.tabpat.service;


import com.example.tabpat.domain.FileUploadDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.form.FileUploadForm;
import com.example.tabpat.util.BeanCopierUtil;
import com.example.tabpat.util.PrimaryKeyUtil;
import com.google.protobuf.ServiceException;
import org.springframework.core.io.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传
 */
@Service
public class FileUploadService extends BaseService {
    //文件上传状态
    private final Map<String, int[]> uploadProgress = new HashMap<String, int[]>();

    /**
     * 文件上传
     *
     * @param fileUploadForm
     * @return
     * @throws ServiceException
     */
    @Transactional
    public Result uploadChunk(FileUploadForm fileUploadForm) throws ServiceException {
        try {
            String fileHash = fileUploadForm.getFileHash();
            int chunkIndex = fileUploadForm.getChunkIndex();
            int totalChunks = fileUploadForm.getTotalChunks();
            MultipartFile file = fileUploadForm.getFile();
            String fileName = fileUploadForm.getFileName();
            //用户获取
            UserDo userDo = userDao.getUserByName(getCurrentUsername());
            String userId = userDo.getUserId();
            //获取文件夹
            File directory = new File("");
            String uploadDir = directory.getCanonicalPath() + "\\" + userId;
            int[] progress = uploadProgress.computeIfAbsent(fileHash, k -> new int[totalChunks]);
            Map<String, Integer> processMap = new HashMap<>();
            if (progress[chunkIndex] == 1) {
                processMap.put("progress", getNextChunkIndex(progress));
                return Result.success(200, "fileUpload", processMap);
            }

            //保存分片
            String filePath = uploadDir + "/" + fileName;
            File chunkFile = new File(filePath + ".part" + chunkIndex);
            try (FileOutputStream out = new FileOutputStream(chunkFile)) {
                out.write(file.getBytes());
            }
            progress[chunkIndex] = 1;

            if (isUploadComplete(progress)) {
                mergeChunk(uploadDir, fileName, totalChunks);
                uploadProgress.remove(fileHash);
            }
            processMap.put("progress", getNextChunkIndex(progress));
            //暂定功能，未上传不保存到数据库
            if (getNextChunkIndex(progress) == -1) {
                FileUploadDo fileUploadDo = buildFileUploadSave(fileUploadForm, userId, filePath);
                fileUploadDao.insert(fileUploadDo);
            }
            return Result.success(200, "file uploaded", processMap);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 文件下载
     */
    public ResponseEntity<Resource> fileDownload(String fileId, HttpServletResponse response) throws ServiceException {
        try {
            FileUploadDo fileUploadDo = fileUploadDao.selectByFileId(fileId);
            //读取到流中
            InputStream inputStream = new FileInputStream(fileUploadDo.getFile());
            response.reset();
            response.setContentType("application/octet-stream");
            String fileName = fileUploadDo.getFileName();
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();

            ByteArrayResource resource = new ByteArrayResource(buffer);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileId);
            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 数据库保存信息
     */

    private FileUploadDo buildFileUploadSave(FileUploadForm fileUploadForm, String userId, String filePath) {
        FileUploadDo fileUploadDo = BeanCopierUtil.create(fileUploadForm, FileUploadDo.class);
        String fileId = PrimaryKeyUtil.get();
        fileUploadDo.setFileId(fileId);
        fileUploadDo.setFile(filePath);
        fileUploadDo.setFileName(fileUploadForm.getFileName());
        fileUploadDo.setUserId(userId);
        return fileUploadDo;
    }

    /**
     * 分片进度
     *
     * @param progress
     * @return
     */
    private int getNextChunkIndex(int[] progress) {
        for (int i = 0; i < progress.length; i++) {
            if (progress[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean isUploadComplete(int[] progress) {
        for (int status : progress) {
            if (status == 0) {
                return false;
            }
        }
        return true;
    }

    private void mergeChunk(String uploadDir, String fileName, int totalChunks) throws IOException {
        File mergedFile = new File(uploadDir + "/" + fileName);
        try (FileOutputStream out = new FileOutputStream(mergedFile)) {
            for (int i = 0; i < totalChunks; i++) {
                File chunkFile = new File(uploadDir + "/" + fileName + ".part" + i);
                byte[] chunkData = java.nio.file.Files.readAllBytes(chunkFile.toPath());
                out.write(chunkData);
                boolean deleted = chunkFile.delete();
                if (!deleted) {
                    // 或者抛出异常
                    throw new IOException("删除文件失败：" + chunkFile.getAbsolutePath());
                }
            }
        }

    }

}
