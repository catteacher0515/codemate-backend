package com.pingyu.codematebackend.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 【V3 蓝图】更新用户头像的“数据包” (DTO)
 * 专门用于接收前端传来的 JSON 请求体
 */
@Data
public class UserUpdateAvatarRequest implements Serializable {

    /**
     * 要更新的用户 ID
     * (注意：在真实生产中，这个 ID 应该从“当前登录用户”中获取，
     * 而不是让前端“告诉”我们。但我们暂时先用这个简单方案。)
     */
    private Long userId;

    /**
     * 新的头像 URL (来自 OSS)
     */
    private String avatarUrl;

    private static final long serialVersionUID = 1L;
}