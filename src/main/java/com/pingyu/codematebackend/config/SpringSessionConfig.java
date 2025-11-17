package com.pingyu.codematebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 【SOP】分布式 Session 共享配置
 */
@Configuration
@EnableRedisHttpSession // <-- 【“分布式 Session”总开关】
public class SpringSessionConfig {
    // 这个类不需要任何内容
    // 只要这个注解“激活”，【依赖层】的“魔法包”就会自动开始工作。
}