package com.pingyu.codematebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pingyu.codematebackend.common.BaseResponse;
import com.pingyu.codematebackend.common.ErrorCode;
import com.pingyu.codematebackend.dto.ChatRequest;
import com.pingyu.codematebackend.dto.ChatVO;
import com.pingyu.codematebackend.exception.BusinessException;
import com.pingyu.codematebackend.model.TeamChat;
import com.pingyu.codematebackend.model.User;
import com.pingyu.codematebackend.model.UserTeamRelation;
import com.pingyu.codematebackend.service.TeamChatService;
import com.pingyu.codematebackend.service.UserService;
import com.pingyu.codematebackend.service.UserTeamRelationService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【案卷 #011】队伍聊天室控制器 (WebSocket + HTTP)
 */
@RestController
public class ChatController {

    @Resource
    private TeamChatService teamChatService;

    @Resource
    private UserService userService;

    @Resource
    private UserTeamRelationService userTeamRelationService;

    // 【核心装备】消息广播器
    @Resource
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 【HTTP】获取历史消息
     * GET /api/chat/history?teamId=1
     */
    @GetMapping("/chat/history")
    public BaseResponse<List<ChatVO>> getHistoryMessage(@RequestParam Long teamId, HttpServletRequest request) {
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1. HTTP 鉴权
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGGED_IN);
        }
        // 2. 队伍鉴权
        checkIsMember(teamId, loginUser.getId());

        // 3. 查询数据库 (只查最近 30 条)
        QueryWrapper<TeamChat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        queryWrapper.orderByAsc("create_time");
        // (实际生产中通常是分页查，这里为了 MVP 简化)
        // queryWrapper.last("limit 30");

        List<TeamChat> chatList = teamChatService.list(queryWrapper);

        // 4. 转换为 VO
        List<ChatVO> voList = chatList.stream().map(chat -> {
            User sender = userService.getById(chat.getUserId());
            return ChatVO.objToVo(chat, sender, loginUser.getId());
        }).collect(Collectors.toList());

        return BaseResponse.success(voList);
    }

    /**
     * 【WebSocket】发送消息
     * 前端发送地址: /app/chat/{teamId}
     * @param teamId 路径参数
     * @param chatRequest 消息体
     * @param headerAccessor 用于获取 WebSocket Session (里面的 loginUser)  <-- 这里加个空格
     */
    @MessageMapping("/chat/{teamId}")
    public void sendMessage(@DestinationVariable Long teamId,
                            @Payload ChatRequest chatRequest,
                            SimpMessageHeaderAccessor headerAccessor) {

        // 1. 从 WebSocket Session 中提取“偷渡”过来的 loginUser
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        User loginUser = (User) sessionAttributes.get("loginUser");

        if (loginUser == null) {
            // (Socket 异常处理比较特殊，这里暂时只能打日志，无法直接返回 HTTP 401)
            System.err.println("【WebSocket】未登录用户尝试发送消息，已拦截");
            return;
        }

        // 2. 校验：是否是该队伍成员
        // (防止有人知道了队伍ID就随便发消息)
        checkIsMember(teamId, loginUser.getId());

        // 3. 【持久化】存入 MySQL
        TeamChat teamChat = new TeamChat();
        teamChat.setTeamId(teamId);
        teamChat.setUserId(loginUser.getId());
        teamChat.setContent(chatRequest.getContent());
        teamChat.setCreateTime(LocalDateTime.now());
        teamChatService.save(teamChat);

        // 4. 【广播】推送到所有订阅者
        // 构造 VO
        ChatVO chatVO = ChatVO.objToVo(teamChat, loginUser, loginUser.getId());
        // 这里有一个小技巧：广播出去的消息，isMine 字段对接收者来说是不确定的
        // (接收者前端会根据自己的 ID 重新判断 isMine，所以这里 VO 的 isMine 设什么都可以)

        // 目标频道: /topic/team/{teamId}
        String destination = "/topic/team/" + teamId;
        messagingTemplate.convertAndSend(destination, chatVO);
    }

    // --- 辅助方法 ---
    private void checkIsMember(Long teamId, Long userId) {
        QueryWrapper<UserTeamRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        queryWrapper.eq("userId", userId);
        long count = userTeamRelationService.count(queryWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.NO_AUTH, "非队伍成员无法查看/发送消息");
        }
    }
}