package com.pingyu.codematebackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 队伍标签关系表
 * 【【【 案卷 #15：已重构 & 清理 】】】
 * @TableName team_tag_relation
 */
@TableName(value ="team_tag_relation")
@Data
public class TeamTagRelation implements Serializable {

    @TableField(exist = false) // 【【 修复：`static` 字段无需 @TableField 】】
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 队伍id (外键, 关联 team.id)
     */
    // 【【 修复：必须使用 camelCase (teamId) 】】
    @TableField(value = "teamId")
    private Long teamId;

    /**
     * 标签id (外键, 关联 tag.id)
     */
    // 【【 修复：必须使用 camelCase (tagId) 】】
    @TableField(value = "tagId")
    private Long tagId;

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