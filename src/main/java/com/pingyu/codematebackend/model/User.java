package com.pingyu.codematebackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户表
 * 【“案子八” - V3 终极修复】
 *
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {

    /**
     * 【3. 添加“序列化版本号”】
     * 这是 Java 序列化机制要求的“身份证号”。
     * 加上它，可以避免未来你修改类（比如增加字段）时导致的反序列化错误。
     * 直接复制 1L 就行。
     */
//    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    // (确保你 @Data 注解是开着的, 或者为它们生成 getter/setter)

    // 【【【 v2.0 升级：添加这些字段 】】】
    @TableField(exist = false)
    private String bio;

    @TableField(exist = false)
    private java.util.List<String> tags;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    @TableField("userAccount")
    private String userAccount;

    /**
     * 头像
     */
    @TableField("avatarUrl")
    private String avatarUrl;

    /**
     * 性别 (0-女, 1-男)
     */
    private Integer gender;

    /**
     * 密码
     */
    @TableField("userPassword")
    private String userPassword;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0-正常
     */
    @TableField("userStatus")
    private Integer userStatus;

    /**
     * 用户角色 0-普通用户 1-管理员
     */
    @TableField("userRole")
    private Integer userRole;

    /**
     * 星球编号
     */
    @TableField("planetCode")
    private String planetCode;
    /**
     * 创建时间
     */
    @TableField("createTime")
    private transient LocalDateTime createTime; // <-- 【【【“第三振”假设：在这里添加 transient】】】

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private transient LocalDateTime updateTime; // <-- 【【【“第三振”假设：在这里添加 transient】】】

    /**
     * 是否删除
     */
    @TableField("isDelete")
    private Integer isDelete;

}