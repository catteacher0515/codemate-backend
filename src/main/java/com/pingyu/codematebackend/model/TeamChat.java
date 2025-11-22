package com.pingyu.codematebackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 队伍聊天记录表
 */
@TableName(value = "team_chat")
@Data
public class TeamChat implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long teamId;
    private Long userId;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}