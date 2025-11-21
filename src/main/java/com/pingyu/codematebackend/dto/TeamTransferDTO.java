package com.pingyu.codematebackend.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 【案卷 #010】转让队长请求体
 * (SOP 1 契约: POST /api/team/transfer)
 */
@Data
public class TeamTransferDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 队伍ID (必需)
     */
    private Long teamId;

    /**
     * 新队长的用户ID (必需)
     */
    private Long newCaptainId;
}