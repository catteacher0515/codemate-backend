package com.pingyu.codematebackend.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 解散队伍请求体
 * 案卷 #009
 */
@Data
public class TeamDeleteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 队伍 id (必需)
     */
    private Long id;
}