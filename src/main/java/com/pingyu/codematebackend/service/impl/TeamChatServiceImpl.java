package com.pingyu.codematebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pingyu.codematebackend.mapper.TeamChatMapper;
import com.pingyu.codematebackend.model.TeamChat;
import com.pingyu.codematebackend.service.TeamChatService;
import org.springframework.stereotype.Service;

/**
 * 队伍聊天服务实现
 */
@Service
public class TeamChatServiceImpl extends ServiceImpl<TeamChatMapper, TeamChat>
        implements TeamChatService {
}