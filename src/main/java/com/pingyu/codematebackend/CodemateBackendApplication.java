package com.pingyu.codematebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
// 1. 导入“排除”工具
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
// 2. 导入“CORS 豁免”(“核武器”““也””需要它！)
import com.pingyu.codematebackend.config.CorsConfig;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 【“案子 15.3” - “环境层·第三振” 修复 A】
 *
 * 我们使用 (exclude = SecurityAutoConfiguration.class)
 * 彻底从“启动项”中“排除”掉 Spring Security 的“自动配置”。
 * (但我们““保留”” @Import CorsConfig！)
 */
@SpringBootApplication(
        // 【【【“核武器”在此！】】】
        exclude = { SecurityAutoConfiguration.class }
)
@EnableScheduling // 【【 2. 开启“定时任务”的总开关 】】
@MapperScan("com.pingyu.codematebackend.mapper")
@Import({
        CorsConfig.class // “CORS 豁免”
        // (我们“不再”需要 SecurityConfig 了！)
})
public class CodemateBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodemateBackendApplication.class, args);
    }

}