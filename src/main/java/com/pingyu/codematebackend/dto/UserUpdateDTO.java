package com.pingyu.codematebackend.dto;

import lombok.Data;

/**
 * 用户更新信息 DTO (Data Transfer Object)
 * 专门用于接收前端的个人信息修改请求
 */
@Data
public class UserUpdateDTO {

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 头像 URL
     */
    private String avatarUrl;

    // 注意：
    // 1. 这里 *绝不* 包含 id, password, salt, role, createTime, isDeleted 等字段。
    // 2. (进阶) 你可以在这里添加 @NotBlank, @Email, @Size 等注解，
    //    然后在 Controller 方法参数上加 @Valid 来开启自动格式校验。
}