package com.example.tabpat.service;

import com.example.tabpat.domain.UserDo;
import com.example.tabpat.form.FileUploadForm;
import com.google.protobuf.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传
 */
@Service
public class FileUploadService extends BaseService {
    //文件上传状态
    private final Map<String, int[]> uploadProgress = new HashMap<String, int[]>();


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
            if (progress[chunkIndex] == 1){
                return Result.success(200,"fileUpload");
            }

            //保存分片
            File chunkFile = new File(uploadDir + "/" + fileName + ".part" + chunkIndex);
            try (FileOutputStream out = new FileOutputStream(chunkFile)) {
                out.write(file.getBytes());
            }
            progress[chunkIndex] = 1;

            if (isUploadComplete(progress)){
                mergeChunk(uploadDir, fileName, totalChunks);
                uploadProgress.remove(fileHash);

            }
            return Result.success(200, "file uploaded");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
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
