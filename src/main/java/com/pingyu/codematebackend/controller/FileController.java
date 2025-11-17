package com.pingyu.codematebackend.controller;

import com.pingyu.codematebackend.common.BaseResponse; // <-- [重构] 导入 BaseResponse
import com.pingyu.codematebackend.common.ErrorCode;     // <-- [重构] 导入 ErrorCode
// import com.pingyu.codematebackend.common.ResultUtils; // <-- [重构] 删除 ResultUtils
import com.pingyu.codematebackend.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 文件上传 指挥官 (Controller)
 * [已重构为策略 B]
 */
@RestController
@RequestMapping("/file")
@Tag(name = "2. 文件上传接口 (FileController)")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 文件上传接口 (接待员)
     */
    @PostMapping("/upload")
    @Operation(
            summary = "通用文件上传 (multipart/form-data)",
            description = "上传文件到阿里云 OSS，返回文件的公网 URL。请使用 form-data 格式。"
    )
    public BaseResponse<String> uploadFile(
            @RequestPart("file") MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {
            // [重构] 使用 BaseResponse.error(ErrorCode, message)
            return BaseResponse.error(ErrorCode.PARAMS_ERROR, "上传的文件不能为空");
        }

        String url;
        try {
            // Service 接口返回 String
            url = fileService.uploadFile(file);
        } catch (Exception e) {
            // (注意：这里的 e.printStackTrace() 最好替换为 log.error())
            e.printStackTrace();

            // [重构] 使用 BaseResponse.error(ErrorCode, message)x
            // 我们使用通用的系统错误码，并附加上具体的异常信息
            return BaseResponse.error(ErrorCode.SYSTEM_ERROR, "上传失败：" + e.getMessage());
        }

        // [重构] 使用 BaseResponse.success(data)
        return BaseResponse.success(url);
    }
}