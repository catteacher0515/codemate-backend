-- 【案卷 #15】 1. 队伍表 (team)
-- (已修复：`userId` 不应为 `auto_increment`)
create table if not exists codemate_backend_db_dev.team
(
    id          bigint auto_increment comment '队伍id' primary key,
    name        varchar(256)                         not null comment '队伍名称',
    description varchar(1024)                        null comment '队伍描述',
    maxNum      int      default 5                   not null comment '最大人数 (已裁决: 5人)',
    expireTime  datetime                             null comment '过期时间',
    userId      bigint                               not null comment '队长id (关联 user.id)',
    status      int      default 0                   not null comment '队伍状态 (0-公开, 1-私有, 2-加密)',
    password    varchar(256)                         null comment '队伍密码 (仅 status=2 时有效)',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                   not null comment '是否删除'
) comment '队伍表';


-- 【案卷 #15】 2. 用户-队伍关系表 (user_team_relation)
-- (已修复：`unique` 索引 必须包含 `userId` 和 `teamId`)
create table if not exists codemate_backend_db_dev.user_team_relation
(
    id          bigint auto_increment comment 'id' primary key,
    userId      bigint                               not null comment '用户id (外键, 关联 user.id)',
    teamId      bigint                               not null comment '队伍id (外键, 关联 team.id)',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                   not null comment '是否删除',
    constraint uniIdx_userId_teamId
        unique (userId, teamId)
) comment '用户队伍关系表';


-- 【案卷 #15】 3. 队伍-标签关系表 (team_tag_relation)
-- (已修复：`tagId` 的类型必须是 `bigint`)
create table if not exists codemate_backend_db_dev.team_tag_relation
(
    id          bigint auto_increment comment 'id' primary key,
    teamId      bigint                               not null comment '队伍id (外键, 关联 team.id)',
    tagId       bigint                               not null comment '标签id (外键, 关联 tag.id)',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                   not null comment '是否删除',
    constraint uniIdx_teamId_tagId
        unique (teamId, tagId)
) comment '队伍标签关系表';