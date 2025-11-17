package com.pingyu.codematebackend.service.impl;

// (导入我们所有的“作案工具”)
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils; // [SOP 4] 导入
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pingyu.codematebackend.common.ErrorCode;
import com.pingyu.codematebackend.dto.TeamCreateDTO;
import com.pingyu.codematebackend.dto.TeamVO;
import com.pingyu.codematebackend.exception.BusinessException;
import com.pingyu.codematebackend.model.Tag; // [SOP 4] 导入
import com.pingyu.codematebackend.model.Team;
import com.pingyu.codematebackend.model.User;
import com.pingyu.codematebackend.model.TeamTagRelation; // [SOP 4] 导入
import com.pingyu.codematebackend.model.UserTeamRelation;
import com.pingyu.codematebackend.service.*;
import com.pingyu.codematebackend.mapper.TeamMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List; // [SOP 4] 导入
import java.util.stream.Collectors;

/**
 * 队伍服务实现
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private UserTeamRelationService userTeamRelationService;

    @Resource
    private UserService userService;

    // 【【【 1. 注入“SOP 4”的新“施工队” 】】】
    @Resource
    private TeamTagRelationService teamTagRelationService;

    @Resource
    private TagService tagService;

    /**
     * 【【 案卷 #17：V3.1 精确实现 (获取队伍详情) 】】】
     */
    @Override
    public TeamVO getTeamDetails(long teamId, User loginUser) {
        // 1. (查询) 队伍基础信息
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "队伍不存在");
        }

        // 2. (权限校验 - 简化版)
        // ( ... )

        // 3. (关键) 实体 -> 视图 (数据合约转换)
        TeamVO teamVO = new TeamVO();
        BeanUtils.copyProperties(team, teamVO);

        // 4. 【【【 修复：线索 1 (Date) 】】】
        // (假设你已修复 TeamVO.java, 此处不再需要 Date.from() 转换)
        // (LocalDateTime -> LocalDateTime, 自动复制)


        // 5. 【【【 聚合：线索 2 (队长) & 修复：线索 3 (脱敏) 】】】
        Long creatorId = team.getUserId();
        User creator = userService.getById(creatorId);
        // 【【【 修复：不再调用 userService.getSafetyUser 】】】
        // 我们调用“本地”的私有脱敏方法
        User safetyCreator = this.getSafetyUser(creator);
        teamVO.setTeamCaptain(safetyCreator);


        // 6. 【【【 聚合：线索 3 (标签) & 修复：线索 4 (getTagName) 】】】
        QueryWrapper<TeamTagRelation> tagQuery = new QueryWrapper<>();
        tagQuery.eq("teamId", teamId);
        List<Long> tagIds = teamTagRelationService.list(tagQuery)
                .stream()
                .map(TeamTagRelation::getTagId)
                .toList();

        if (!tagIds.isEmpty()) {
            List<String> tagNames = tagService.listByIds(tagIds)
                    .stream()
                    // 【【【 修复：Tag::getTagName -> Tag::getTagname 】】】
                    // (基于 Tag.java 的 'tagname' 字段)
                    .map(Tag::getTagname)
                    .toList();
            teamVO.setTags(tagNames);
        } else {
            teamVO.setTags(List.of());
        }


        // 7. 【【【 聚合：线索 5 (成员) & 修复：线索 3 (脱敏) 】】】
        QueryWrapper<UserTeamRelation> userQuery = new QueryWrapper<>();
        userQuery.eq("teamId", teamId);
        List<Long> memberIds = userTeamRelationService.list(userQuery)
                .stream()
                .map(UserTeamRelation::getUserId)
                .toList();

        if (!memberIds.isEmpty()) {
            List<User> safeMembers = userService.listByIds(memberIds)
                    .stream()
                    // 【【【 修复：调用“本地”的私有脱敏方法 】】】
                    .map(this::getSafetyUser)
                    .toList();
            teamVO.setMembers(safeMembers);
        } else {
            teamVO.setMembers(List.of());
        }

        // 8. (返回) 交付 V3.1 聚合视图
        return teamVO;
    }


    /**
     * 【【【 新增：V3.1 内部脱敏工具 】】】
     * (解决 'getSafetyUser' 无法解析问题)
     *
     * @param originUser 从数据库查出的原始 User 对象
     * @return “安全”的 User 对象 (手动“裁剪”字段)
     */
    private User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safeUser = new User();
        // 1. 复制“安全”字段 (基于 User.java)
        safeUser.setId(originUser.getId());
        safeUser.setUsername(originUser.getUsername());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setBio(originUser.getBio());
        safeUser.setTags(originUser.getTags());

        // 2. “裁剪”危险字段 (userPassword, phone, email, userStatus, etc.)
        //    (我们什么都不做，它们在 new User() 中默认为 null)

        return safeUser;
    }


    /**
     * 【【【 案卷 #16：SOP 5 (事务) 】】】
     * 保证 SOP 2, 3, 4 “同生共死”
     */
    @Transactional(rollbackFor = Exception.class) // (指定对所有异常都“回滚”)
    @Override
    public Long createTeam(TeamCreateDTO teamCreateDTO, User loginUser) {

        // 【【 SOP 1 (校验) 】】
        if (teamCreateDTO == null || loginUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int maxNum = teamCreateDTO.getMaxNum();
        if (maxNum < 2 || maxNum > 5) { // (已裁决)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数必须在 2-5 人");
        }
        LocalDateTime expireTime = teamCreateDTO.getExpireTime();
        if (expireTime != null && expireTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "过期时间不能早于当前时间");
        }
        int status = teamCreateDTO.getStatus();
        String password = teamCreateDTO.getPassword();
        if (status == 2 && (password == null || password.length() < 4)) { // 2=加密
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "加密队伍必须设置至少4位的密码");
        }

        // 【【 SOP 2 (存 team 表) 】】
        Team team = new Team();
        BeanUtils.copyProperties(teamCreateDTO, team);
        team.setUserId(loginUser.getId());

        boolean saveResult = this.save(team);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败 (team 表插入错误)");
        }

        Long newTeamId = team.getId();

        // 【【 SOP 3 (存 user_team 表) 】】
        UserTeamRelation userTeamRelation = new UserTeamRelation();
        userTeamRelation.setUserId(loginUser.getId());
        userTeamRelation.setTeamId(newTeamId);

        boolean relationSaveResult = userTeamRelationService.save(userTeamRelation);
        if (!relationSaveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败 (user_team 关系表插入错误)");
        }

        // 【【【 案卷 #16.2：SOP 4 (存 team_tag 表) 】】】

        // 2. 获取“标签名”列表
        List<String> tagNameList = teamCreateDTO.getTags();

        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 3. “遍历” 列表
            for (String tagName : tagNameList) {

                // 4. 查询“标签 ID”
                // (SOP 优化：我们应该用 `getOne` 而不是 `one`，
                //  `one` 在找到多个时会报错，`getOne` 只取一个)
                Tag tag = tagService.query().eq("tagName", tagName).one();

                if (tag == null) { // (校验)
                    // (SOP 优化：如果标签不存在，我们可以“自动创建”它)
                    // (但为了SOP 16的简洁，我们先“抛错”)
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签 '" + tagName + "' 不存在");
                }

                // 5. “组装” 关系
                TeamTagRelation teamTagRelation = new TeamTagRelation();
                teamTagRelation.setTeamId(newTeamId);
                teamTagRelation.setTagId(tag.getId());

                // 6. “存盘”
                teamTagRelationService.save(teamTagRelation);
                // (因 @Transactional 存在，无需检查返回值)
            }
        }

        // 【【 SOP 6 (返回) 】】
        return newTeamId;
    }


}