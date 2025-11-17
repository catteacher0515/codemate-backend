package com.pingyu.codematebackend.service.impl;

import com.aliyun.oss.OSS;
import com.pingyu.codematebackend.config.OssProperties; // <-- 修正 2: 导入正确的 OssProperties
import com.pingyu.codematebackend.service.FileService; // <-- 修正 3: 导入正确的 FileService
import org.springframework.beans.factory.annotation.Autowired; // <-- 修正 4: 统一使用 @Autowired
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// 移除了不必要的 javax.annotation.Resource
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传服务 实现类 (实干家)
 * 【修正版 - 2025-10-31】
 */
@Service // 声明这是一个 Service Bean，交给 Spring 管理
public class FileServiceImpl implements FileService {

    // 注入“OSS 工厂”制造的“单例” OSS 客户端
    @Autowired // <-- 修正 5: 统一使用 @Autowired
    private OSS ossClient;

    // 注入“信使” (OssProperties)
    @Autowired // <-- 修正 6: 统一使用 @Autowired
    private OssProperties ossProperties;

    /**
     * 履行“服务合同”：上传文件
     */
    @Override
    public String uploadFile(MultipartFile file) {

        // --- 1. 从“信使”那里获取配置信息 ---
        String bucketName = ossProperties.getBucketName();
        String endpoint = ossProperties.getEndpoint();

        // --- 2. [关键] 生成唯一的文件名，防止覆盖 ---

        // 2.1. 获取原始文件名 (e.g., "my_avatar.png")
        String originalFilename = file.getOriginalFilename();

        // 2.2. (专业实践) 按日期归档，防止单个文件夹文件过多
        // e.g., "2025/10/31/"
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));

        // 2.3. 生成 UUID (e.g., "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        String uuid = UUID.randomUUID().toString();

        // 2.4. 提取文件扩展名 (e.g., ".png")
        String fileExtension = "";
        if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 2.5. 拼接新的、唯一的文件名（在 OSS 上的存储路径）
        // e.g., "codemate/avatars/2025/10/31/f47ac10b-58cc-4372-a567-0e02b2c3d479.png"
        // 备注：我们在前面加上 "codemate/avatars/" 目录，以践行我们“通用 Bucket，按项目分目录”的架构
        String objectName = "codemate/avatars/" + datePath + uuid + fileExtension;


        // --- 3. [核心] 执行上传 ---
        try (InputStream inputStream = file.getInputStream()) {

            // 关键操作：调用 OSS SDK 执行上传
            // 参数: (Bucket名称, 存到 OSS 上的唯一文件名, 文件的输入流)
            ossClient.putObject(bucketName, objectName, inputStream);

        } catch (Exception e) {
            // 如果上传过程中出现任何异常...
            e.printStackTrace(); // 打印错误堆栈（方便我们调试）
            // 抛出一个运行时异常，告诉 Controller 上传失败了
            throw new RuntimeException("文件上传到 OSS 失败", e);
        }

        // --- 4. [关键] 拼接并返回文件的公网可访问 URL ---
        // 格式: "https://" + Bucket名称 + "." + Endpoint + "/" + 唯一文件名
        // e.g., "https://pingyu-storage.oss-cn-beijing.aliyuncs.com/codemate/avatars/2025/10/31/f47ac10b....png"
        String fileUrl = "https://" + bucketName + "." + endpoint + "/" + objectName;

        // 把这个 URL 返回给 Controller
        return fileUrl;
    }
}