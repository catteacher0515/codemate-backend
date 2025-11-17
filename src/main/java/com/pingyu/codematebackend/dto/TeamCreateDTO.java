package com.pingyu.codematebackend.dto;

import lombok.Data;
import java.time.LocalDateTime; // <-- [SOP 修正] 使用 Java 8+ 的日期时间
import java.util.List;

/**
 * 【【【 案卷 #15：系统设计 】】】
 * 队伍创建“包裹” (Data Transfer Object)
 * * 专门用于接收前端 /api/team/create 接口发送的 JSON 数据
 */
@Data
public class TeamCreateDTO {

    /**
     * 队伍名称
     * (来自 "实体" 裁决)
     */
    private String name;

    /**
     * 队伍描述
     * (来自 "实体" 裁决)
     */
    private String description;

    /**
     * 最大人数
     * (已裁决：上限 5 人)
     */
    private int maxNum;

    /**
     * 过期时间
     * (使用 LocalDateTime 来匹配数据库)
     */
    private LocalDateTime expireTime;

    /**
     * 队伍状态
     * (0-公开, 1-私有, 2-加密)
     */
    private int status;

    /**
     * 队伍密码
     * (仅在 status 为 2 时有效，否则可为 null)
     */
    private String password;

    /**
     * 队伍标签列表
     * (Service 将负责处理这个列表并存入 team_tag_relation)
     */
    private List<String> tags;

    // --- “陷阱” ---
    //
    // DTO 中 *绝不* 包含 userId（队长ID）。
    // 队长 ID 必须从“可信”的 `HttpSession` 中获取，
    // 以防止“越权”漏洞。

}