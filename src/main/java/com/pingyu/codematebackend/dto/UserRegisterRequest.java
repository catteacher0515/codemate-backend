package com.pingyu.codematebackend.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 【22号案】用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L; // (保持SOP)

    private String userAccount;

    private String userPassword;

    private String checkPassword; // (用于二次确认密码)
}