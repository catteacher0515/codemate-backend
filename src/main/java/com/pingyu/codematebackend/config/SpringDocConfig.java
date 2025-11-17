package com.pingyu.codematebackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 【“案子十” - API 档案配置】
 * (Knife4j / SpringDoc 核心配置)
 */
// ... (其他 import ...)
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        // 【【【“案子十” - 最终修复：“笔误”纠正！】】】
                        .title("CodeMate (码缘) - 伙伴匹配系统 API 文档") // <-- “猿” (Ape) 已改为 “缘” (Destiny)！
                        .version("v0.1.0")
                        .description("这是 CodeMate 伙伴匹配系统的“后端 API”文档。")
                        .contact(new Contact()
                                        .name("萍雨 (侦探)")
                                // ...
                        )
                );
    }
}