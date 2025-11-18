// file: com.pingyu.codematebackend.config.MyBatisPlusConfig.java

package com.pingyu.codematebackend.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 【【【 案卷 #18：V3.x 环境修复 】】】
 * 注册“MyBatis-Plus 分页拦截器”
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 注入“分页引擎” (PaginationInnerInterceptor)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 【【【 V3.x 修复：添加“分页引擎” 】】】
        // (我们 100% 知道数据库是 MySQL)
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        return interceptor;
    }
}