package com.pingyu.codematebackend.dto;

import com.pingyu.codematebackend.model.User; // (你需要导入你的 UserVO/SafetyUser)
import lombok.Data;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

/**
 * 队伍“证物袋” (View Object)
 * 这是“安全”地返回给前端的“组装”对象
 */
@Data
public class TeamVO {

    // --- 1. 队伍自身的信息 (来自 team 表) ---
    private Long id;
    private String name;
    private String description;
    private Integer maxNum;
    private Long userId; // (队长 ID)
    private Integer status;
    private List<String> tags; // (假设你的 Team 实体里 'tags' 是 List<String>)
    private LocalDateTime expireTime; // <--- [修复] 从 Date 改为 LocalDateTime
    private LocalDateTime createTime; // <--- [修复] 从 Date 改为 LocalDateTime

    // --- 2. 队长的信息 (来自 user 表的“聚合”) ---
    // (我们复用 UserService 里的“脱敏”用户对象，
    //  这里我假设它叫 UserVO 或 SafetyUser，你可能需要修改)
    private User teamCaptain;

    // --- 3. 成员列表信息 (来自 user_team 表的“聚合”) ---
    private List<User> members;
}