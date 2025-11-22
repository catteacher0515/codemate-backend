package com.pingyu.codematebackend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pingyu.codematebackend.common.BaseResponse;
import com.pingyu.codematebackend.controller.ChatController;
import com.pingyu.codematebackend.dto.ChatRequest;
import com.pingyu.codematebackend.dto.ChatVO;
import com.pingyu.codematebackend.model.TeamChat;
import com.pingyu.codematebackend.model.User;
import com.pingyu.codematebackend.service.TeamChatService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * 【案卷 #011】L2 集成测试：聊天室业务逻辑
 * 目标：验证 "鉴权 -> 存库 -> 广播" 这一条龙服务是否通畅
 */
@SpringBootTest
public class ChatL2Test {

    @Resource
    private ChatController chatController;

    @Resource
    private TeamChatService teamChatService;

    // 【关键】Mock 掉消息发送模板，我们不真的发网络包，只验证"它被调用了"
    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 测试场景：发送消息 (WebSocket)
     * 预期：数据库新增1条记录，且广播方法被调用
     */
    @Test
    @Transactional // 测试完回滚数据，保持现场干净
    void testSendMessage_Flow() {
        // --- 1. 准备 (Arrange) ---
        Long teamId = 1L; // 假设队伍1存在
        Long userId = 8L; // 假设用户1是队伍成员(大剑)
        String content = "Hello L2 Test " + System.currentTimeMillis();

        // 1.1 构造请求
        ChatRequest request = new ChatRequest();
        request.setTeamId(teamId);
        request.setContent(content);

        // 1.2 【核心】伪造 WebSocket 握手后的 Session
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        Map<String, Object> attributes = new HashMap<>();
        User loginUser = new User();
        loginUser.setId(userId);
        loginUser.setUsername("TestUser");
        loginUser.setAvatarUrl("test.png");
        attributes.put("loginUser", loginUser); // 塞入“偷渡”的用户
        headerAccessor.setSessionAttributes(attributes);

        // --- 2. 行动 (Act) ---
        System.out.println("--- [L2] 正在模拟发送消息: " + content);
        chatController.sendMessage(teamId, request, headerAccessor);

        // --- 3. 断言 (Assert) ---

        // 3.1 验证数据库：真的存进去了吗？
        QueryWrapper<TeamChat> qw = new QueryWrapper<>();
        qw.eq("content", content);
        TeamChat savedChat = teamChatService.getOne(qw);
        assertNotNull(savedChat, "数据库应该能查到刚才发的消息");
        assertEquals(userId, savedChat.getUserId(), "发送者ID应一致");
        System.out.println("--- [L2] 数据库验证通过: ID=" + savedChat.getId());

        // 3.2 验证广播：真的通知大家了吗？
        // 验证 convertAndSend("/topic/team/1", chatVO) 是否被调用了一次
        verify(messagingTemplate, Mockito.times(1))
                .convertAndSend(eq("/topic/team/" + teamId), any(ChatVO.class));
        System.out.println("--- [L2] 广播调用验证通过");
    }

    /**
     * 测试场景：获取历史消息 (HTTP)
     * 预期：能查到刚才存的消息
     */
    @Test
    @Transactional
    void testGetHistory_Flow() {
        // --- 1. 准备数据 ---
        Long teamId = 1L;
        Long userId = 8L;
        // 先插一条数据
        TeamChat chat = new TeamChat();
        chat.setTeamId(teamId);
        chat.setUserId(userId);
        chat.setContent("History Check");
        teamChatService.save(chat);

        // --- 2. 构造 HTTP 请求 ---
        MockHttpServletRequest request = new MockHttpServletRequest();
        User loginUser = new User();
        loginUser.setId(userId);
        request.getSession().setAttribute("loginUser", loginUser);

        // --- 3. 调用接口 ---
        BaseResponse<List<ChatVO>> response = chatController.getHistoryMessage(teamId, request);

        // --- 4. 断言 ---
        assertNotNull(response.getData());
        assertTrue(response.getData().size() > 0);
        // 验证最新的一条是不是我们插的
        boolean found = response.getData().stream()
                .anyMatch(vo -> vo.getContent().equals("History Check"));
        assertTrue(found, "历史记录里应该包含刚才插入的消息");
        System.out.println("--- [L2] 历史消息查询验证通过，条数: " + response.getData().size());
    }
}