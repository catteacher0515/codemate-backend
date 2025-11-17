package com.pingyu.codematebackend.service;

import com.pingyu.codematebackend.dto.TeamCreateDTO;
import com.pingyu.codematebackend.dto.TeamVO;
import com.pingyu.codematebackend.model.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pingyu.codematebackend.model.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author 花萍雨
* @description 针对表【team(队伍表)】的数据库操作Service
* @createDate 2025-11-06 17:41:58
*/
public interface TeamService extends IService<Team> {
    public Long createTeam(TeamCreateDTO teamCreateDTO, User loginUser);

    /**
     * 获取队伍详情（包含创建人信息、权限校验）
     *
     * @param teamId    队伍 ID
     * @param loginUser 当前登录用户 (用于权限校验)
     * @return TeamVO 或 null
     */
    TeamVO getTeamDetails(long teamId, User loginUser);
}
