package com.pingyu.codematebackend.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired; // <--- 修正点 1：使用这个 import
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 注意：我们不再需要 import javax.annotation.Resource;

/**
 * 【模板二：Bean 配置类 (工厂)】
 *
 * 目标：创建并配置一个“单例”的 OSS 客户端 Bean，
 * 交给 Spring 容器统一管理。
 */
@Configuration // 声明这是一个“工厂”类
public class OssConfig {

    // 注入我们的“信使” (OssProperties)，
    // 它已经携带了 .yml 中的所有配置信息
    @Autowired // <--- 修正点 2：使用 @Autowired 替代 @Resource
    private OssProperties ossProperties;

    /**
     * @Bean 注解告诉 Spring：“请执行这个方法，
     * 并把这个方法返回的对象（那个 OSS 客户端）
     * 当作一个 Bean 来管理。”
     * <p>
     * Bean 的名字默认就是方法名 "ossClient"。
     *
     * @return 一个配置好、可用的 OSS 客户端实例
     */
    @Bean
    public OSS ossClient() {
        // 使用“信使”携带的配置信息来创建 OSSClient
        return new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret()
        );
    }
}