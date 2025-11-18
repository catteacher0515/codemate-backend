package com.pingyu.codematebackend.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 【【【 案卷 #004：加入队伍合约 (DTO) 】】】
 * (SOP 1 契约)
 */
@Data
public class TeamJoinDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 必需, 队伍ID
     */
    private Long teamId;

    /**
     * 可选, 加密队伍时必需
     */
    private String password;
}