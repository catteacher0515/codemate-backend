package com.pingyu.codematebackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 配置属性类
 *
 * 这个类作为“信使”，负责从 application.yml 文件中读取 aliyun.oss.* 下的配置
 * 并将它们注入到类的字段中，以便其他 Service 或 Controller 使用。
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Data // Lombok 注解：自动生成 getter/setter/toString 等方法
public class OssProperties {

    /**
     * OSS 的 Endpoint (访问域名)
     * 会自动读取 application.yml 中的 aliyun.oss.endpoint
     */
    private String endpoint;

    /**
     * RAM 用户的 AccessKey ID
     * 会自动读取 application.yml 中的 aliyun.oss.access-key-id
     */
    private String accessKeyId;

    /**
     * RAM 用户的 AccessKey Secret
     * 会自动读取 application.yml 中的 aliyun.oss.access-key-secret
     */
    private String accessKeySecret;

    /**
     * Bucket 名称
     * 会自动读取 application.yml 中的 aliyun.oss.bucket-name
     */
    private String bucketName;
}