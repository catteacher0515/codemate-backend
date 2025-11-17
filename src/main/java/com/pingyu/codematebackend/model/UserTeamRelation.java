package com.pingyu.codematebackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户队伍关系表
 * 【【【 案卷 #15：已重构 & 清理 】】】
 * @TableName user_team_relation
 */
@TableName(value ="user_team_relation")
@Data
public class UserTeamRelation implements Serializable {

    @TableField(exist = false) // 【【 修复：`static` 字段无需 @TableField 】】
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id (外键, 关联 user.id)
     */
    // 【【 修复：必须使用 camelCase (userId) 】】
    @TableField(value = "userId")
    private Long userId;

    /**
     * 队伍id (外键, 关联 team.id)
     */
    // 【【 修复：必须使用 camelCase (teamId) 】】
    @TableField(value = "teamId")
    private Long teamId;

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

    // 【【 修复：删除所有手写的 equals, hashCode, toString,
    //     因为 @Data 已经自动生成了它们 】】
}