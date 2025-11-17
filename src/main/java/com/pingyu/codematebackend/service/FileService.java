package com.pingyu.codematebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pingyu.codematebackend.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List; // 确保导入 List

/**
 * @author 花萍雨
 * @description 专门处理用户和文件相关的操作
 * @since 2025-10-26 // Javadoc 标签修正
 */

/**
 * 文件上传服务 接口 (服务合同)
 */

public interface FileService {

    /**
     * 上传文件到 OSS
     *
     * @param file (来自 Controller 的) 文件对象
     * @return (返回给 Controller 的) 文件在 OSS 上的可访问 URL
     */
    String uploadFile(MultipartFile file);
    // 返回的数据类型是 String
    // 然后 Controller 层会把 String 变成一个包含 URL 的响应体给前端
    // 前端传递给的 Controller 层的内容是一个文件,作为参数
    // 文件的话,我们就需要使用 SpringMVC 当中的 MultipartFile 类
}