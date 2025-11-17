package com.pingyu.codematebackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 队伍表
 * 【【【 案卷 #15：已重构 & 清理 】】】
 * @TableName team
 */
@TableName(value ="team")
@Data
public class Team implements Serializable {

    /**
     * 队伍id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 队伍名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 队伍描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 最大人数 (已裁决: 5人)
     */
    // 【【 修复：必须使用 camelCase (maxNum) 】】
    @TableField(value = "maxNum")
    private Integer maxNum;

    /**
     * 过期时间
     */
    // 【【 修复：必须使用 camelCase (expireTime) 】】
    @TableField(value = "expireTime")
    private LocalDateTime expireTime;

    /**
     * 队长id (关联 user.id)
     */
    // 【【 修复：必须使用 camelCase (userId) 】】
    @TableField(value = "userId")
    private Long userId;

    /**
     * 队伍状态 (0-公开, 1-私有, 2-加密)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 队伍密码 (仅 status=2 时有效)
     */
    @TableField(value = "password")
    private String password;

    /**
     * 创建时间
     */
    // 【【 修复：必须使用 camelCase (createTime) 】】
    @TableField(value = "createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    // 【【 修复：必须使用 camelCase (updateTime) 】】
    @TableField(value = "updateTime")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    // 【【 修复：必须使用 camelCase (isDelete) 】】
    @TableField(value = "isDelete")
    private Integer isDelete;

    // 【【 修复：static 字段 *不* 需要 @TableField 注解 】】
    private static final long serialVersionUID = 1L;

    // 【【 修复：删除所有手写的 equals, hashCode, toString,
    //     因为 @Data 已经自动生成了它们 】】
}