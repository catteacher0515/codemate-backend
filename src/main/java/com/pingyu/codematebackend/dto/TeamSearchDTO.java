// file: com.pingyu.codematebackend.dto.TeamSearchDTO.java

package com.pingyu.codematebackend.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 【【【 案卷 #18：队伍搜索合约 (DTO) 】】】
 */
@Data
public class TeamSearchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- 1. 搜索关键词 (用于 队伍名称 和 描述) ---
    private String searchText;

    // --- 2. 标签列表 (用于 V3 聚合搜索) ---
    private List<String> tagNames;

    // --- 3. 登录用户 ID (用于过滤掉“我已加入的队伍”) ---
    // (V2 重构点：这个DTO可以由Controller填充，而不是前端)
    // private Long loginUserId;

    // --- 4. MP 分页参数 (必需) ---
    // (MP Page 对象默认就是这2个名字)
    private int current = 1; // 当前页码
    private int pageSize = 10; // 页面大小
}