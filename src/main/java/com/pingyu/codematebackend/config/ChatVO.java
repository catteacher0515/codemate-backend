package com.pingyu.codematebackend.dto;

import com.pingyu.codematebackend.model.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ChatVO implements Serializable {
    private Long id;
    private Long userId;
    private String username;
    private String userAvatar;
    private String content;
    private String createTime; // 格式化后的时间字符串
    private Boolean isMine; // 是否是我发的消息(前端用于区分左右气泡)

    public static ChatVO objToVo(com.pingyu.codematebackend.model.TeamChat chat, User sender, Long currentUserId) {
        ChatVO vo = new ChatVO();
        BeanUtils.copyProperties(chat, vo);
        if (sender != null) {
            vo.setUsername(sender.getUsername());
            vo.setUserAvatar(sender.getAvatarUrl());
        }
        // 格式化时间
        if (chat.getCreateTime() != null) {
            vo.setCreateTime(chat.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        // 判断是否是自己发的
        vo.setIsMine(chat.getUserId().equals(currentUserId));
        return vo;
    }
}