package com.example.tabpat.service;


import com.example.tabpat.domain.FileUploadDo;
import com.example.tabpat.domain.UserDo;
import com.example.tabpat.form.FileUploadForm;
import com.example.tabpat.util.BeanCopierUtil;
import com.example.tabpat.util.PrimaryKeyUtil;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicIntegerArray;


/**
 * 文件上传
 *
 * @author ABin
 */
@Service
public class FileUploadService extends BaseService {
    private static final Logger logger = LogManager.getLogger(FileUploadService.class);

    //文件上传状态
    private final ConcurrentHashMap<String, AtomicIntegerArray> uploadProgress = new ConcurrentHashMap<>();
    // 注入 HttpServletRequest，用于获取部署地址
    private final HttpServletRequest request;

    @Autowired
    public FileUploadService(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 文件上传
     *
     * @param fileUploadForm -文件form表单
     * @return -网络状态码
     * @throws ServiceException -服务错误
     */
    @Transactional(rollbackFor = ServiceException.class)
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
            // 获取上传目录
            Path uploadDirPath = Paths.get(System.getProperty("user.dir"), userId);
            Files.createDirectories(uploadDirPath);
            //进度管理
            AtomicIntegerArray progress = uploadProgress.computeIfAbsent(fileHash, k -> new AtomicIntegerArray(totalChunks));
            // 检查是否已上传
            if (progress.get(chunkIndex) == 1) {
                if (isUploadComplete(progress)) {
                    uploadProgress.remove(fileHash);
                    return Result.success(200, "file uploaded", Map.of("fileId", fileHash));
                }
                return Result.success(200, "fileUpload", Map.of("progress", getNextChunkIndex(progress)));
            }

            // 分片存储文件块
            File chunkFile = new File(uploadDirPath.toString(), fileName + ".part" + chunkIndex);
            Files.copy(file.getInputStream(), chunkFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


            // 更新进度
            progress.set(chunkIndex, 1);

            // 检查是否完成
            if (isUploadComplete(progress)) {
                mergeChunk(uploadDirPath.toString(), fileName, totalChunks);
                uploadProgress.remove(fileHash);
            }

            // 组织返回数据
            Map<String, Object> processMap = new HashMap<>();
            processMap.put("progress", getNextChunkIndex(progress));

            // 若全部上传完毕，记录到数据库
            if (getNextChunkIndex(progress) == -1) {
                Path absolutePath = uploadDirPath.toAbsolutePath();
                String fullFilePath = absolutePath.resolve(fileName).toString();

                FileUploadDo fileUploadDo = buildFileUploadSave(fileUploadForm, userId, fullFilePath);
                fileUploadDao.insert(fileUploadDo);
                // 根据文件类型获取 URL
                String fileUrl = getFileUrl(fileName, userId, fileUploadDo.getFileId());
                processMap.put("fileId", fileUploadDo.getFileId());
                // 返回文件的 URL
                processMap.put("fileUrl", fileUrl);
            }

            return Result.success(200, "file uploaded", processMap);
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            throw new ServiceException(e);
        }
    }

    /**
     * 文件下载
     * 使用nginx更加快速
     */
    public ResponseEntity<Resource> fileDownload(String fileId, HttpServletResponse response) throws ServiceException {
        try {
            FileUploadDo fileUploadDo = fileUploadDao.selectByFileId(fileId);
            File file = new File(fileUploadDo.getFile());

            if (!file.exists()) {
                throw new FileNotFoundException("文件不存在：" + file.getAbsolutePath());
            }

            //使用 InputStreamResource，避免内存占用
            Path filePath = file.toPath();
            Resource resource = new InputStreamResource(Files.newInputStream(filePath));
            //设置响应头
            String encodedFileName = URLEncoder.encode(fileUploadDo.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.addHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(resource);

        } catch (Exception e) {

            logger.error("文件下载失败", e);
            throw new ServiceException("文件下载失败", e);
        }
    }

    private void mergeChunk(String uploadDir, String fileName, int totalChunks) throws IOException {
        File mergedFile = new File(uploadDir + "/" + fileName);
        //使用 Files.newOutputStream() 避免 FileOutputStream 手动 close()
        try (OutputStream out = Files.newOutputStream(mergedFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (int i = 0; i < totalChunks; i++) {
                File chunkFile = new File(uploadDir + "/" + fileName + ".part" + i);
                byte[] chunkData = Files.readAllBytes(chunkFile.toPath());
                out.write(chunkData);

                for (int retry = 0; retry < 3; retry++) {
                    if (chunkFile.delete()) {
                        break;
                    }
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
    private int getNextChunkIndex(AtomicIntegerArray progress) {
        for (int i = 0; i < progress.length(); i++) {
            if (progress.get(i) == 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean isUploadComplete(AtomicIntegerArray progress) {
        for (int i = 0; i < progress.length(); i++) {
            // 线程安全读取
            if (progress.get(i) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据文件类型生成 URL
     *
     * @param fileName 文件名
     * @return 返回文件的 URL
     */
    private String getFileUrl(String fileName, String userId, String fileId) {
        // 获取文件扩展名
        String fileExtension = getFileExtension(fileName);

        String baseUrl = getBaseUrl();
        if (isImageFile(fileExtension)) {
            // 图片展示用 URL
            return baseUrl + userId + "/" + fileName;
        } else {
            // 普通文件下载用 URL
            return baseUrl + "download/" + fileId;
        }
    }

    /**
     * 获取动态的基础 URL（主机名 + 端口号 + 上下文路径）
     *
     * @return 返回动态的基础 URL
     */
    private String getBaseUrl() {
        // 获取协议（http 或 https）
        String scheme = request.getScheme();
        // 获取服务器名（如 localhost 或域名）
        String serverName = request.getServerName();
        // 获取服务器端口号
        int serverPort = request.getServerPort();
        // 获取应用上下文路径（如果有的话）
        String contextPath = request.getContextPath();

        // 根据不同的协议（http 或 https）和端口号动态构建基础 URL
        String port = (serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort;
        return scheme + "://" + serverName + port + contextPath + "/files/";
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 返回文件的扩展名
     */
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }

    /**
     * 判断是否为图片类型
     */
    private boolean isImageFile(String extension) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp", "tiff"};
        for (String ext : imageExtensions) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

}
