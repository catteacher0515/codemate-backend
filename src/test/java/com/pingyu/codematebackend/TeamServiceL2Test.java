package com.pingyu.codematebackend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pingyu.codematebackend.dto.TeamSearchDTO;
import com.pingyu.codematebackend.dto.TeamVO;
import com.pingyu.codematebackend.model.User;
import com.pingyu.codematebackend.service.TeamService;
import com.pingyu.codematebackend.dto.TeamVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 【【【 案卷 #17.3：L2 集成测试 (主攻方案) 】】】
 * * 目标：启动完整 Spring，在“真实数据库”上
 * * 调试 V3.1 聚合逻辑 (getTeamDetails)
 */
@SpringBootTest // (它会启动你完整的 SpringBoot 应用)
public class TeamServiceL2Test {

    @Resource
    private TeamService teamService; // (直接注入“真实”的 Service)

    @Test
    void testGetTeamDetails_L2_Debug() {
        // --- 1. 准备 (Arrange) ---

        // 【【【 确保你的数据库中存在 ID=1 的队伍 】】】
        long teamIdToTest = 1L;

        // 【【【 伪造“登录用户” (解决 Session 摩擦) 】】】
        // (我们绕过了 Controller, 只需给 Service 一个它需要的 User 对象)
        User fakeLoginUser = new User();
        fakeLoginUser.setId(1L); // (假设 1L 是管理员或队长)


        // --- 2. 行动 (Act) ---

        // 【【【 侦探：请在这里设置你的断点 (Breakpoint) 】】】
        System.out.println("--- [L2 调试] 准备进入 TeamService.getTeamDetails ---");

        TeamVO teamVO = teamService.getTeamDetails(teamIdToTest, fakeLoginUser);

        // --- 3. 断言 (Assert) ---
        System.out.println("--- [L2 调试] 聚合查询已返回 ---");
        System.out.println(teamVO);

        // (断言它不为空，且数据正确)
        assert(teamVO != null);
        assert(teamVO.getId() == teamIdToTest);
        assert(teamVO.getTeamCaptain() != null); // 确保队长信息已填充

        System.out.println("--- [L2 调试] 队长: " + teamVO.getTeamCaptain().getUsername());
        System.out.println("--- [L2 调试] 成员数量: " + (teamVO.getMembers() != null ? teamVO.getMembers().size() : 0));
    }

    /**
     * 【【【 案卷 #18：L2 集成测试 (搜索/分页) 】】】
     * * 目标：在“真实数据库”上
     * * 调试 V3.x 聚合搜索 (searchTeams)
     */
    @Test
    void testSearchTeams_L2_Debug() {
        // --- 1. 准备 (Arrange) ---

        // 【【【 1a. 伪造“登录用户” 】】】
        User fakeLoginUser = new User();
        fakeLoginUser.setId(1L); // (假设 1L 已登录)

        // 【【【 1b. 准备“搜索合约” (DTO) 】】】
        TeamSearchDTO dto = new TeamSearchDTO();

        // (*** 侦探：在这里修改你的“搜索条件” ***)
        // dto.setSearchText("萍雨");     // (测试“按名称”)
        dto.setTagNames(List.of("Java")); // (测试“按标签”)

        dto.setCurrent(1);  // (测试“分页”)
        dto.setPageSize(5);

        // --- 2. 行动 (Act) ---

        // 【【【 侦探：请在这里设置你的断点 (Breakpoint) 】】】
        System.out.println("--- [L2 调试] 准备进入 TeamService.searchTeams ---");

        Page<TeamVO> teamPage = teamService.searchTeams(dto, fakeLoginUser);

        // --- 3. 断言 (Assert) ---
        System.out.println("--- [L2 调试] 聚合搜索已返回 ---");
        System.out.println("--- [L2 调试] 总记录数: " + teamPage.getTotal());
        System.out.println("--- [L2 调试] 总页数: " + teamPage.getPages());

        // (打印搜索到的记录)
        teamPage.getRecords().forEach(teamVO -> {
            System.out.println("    [ID: " + teamVO.getId() + "] " + teamVO.getName());
            if (teamVO.getTeamCaptain() != null) {
                System.out.println("       -> 队长: " + teamVO.getTeamCaptain().getUsername());
            }
        });

        // (断言它不为空)
        assert(teamPage != null);
        assert(teamPage.getRecords() != null);

        System.out.println("--- [L2 调试] searchTeams 测试完毕 ---");
    }
}