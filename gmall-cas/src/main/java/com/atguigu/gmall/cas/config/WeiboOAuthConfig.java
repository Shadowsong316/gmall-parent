package com.atguigu.gmall.cas.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@ConfigurationProperties(prefix = "oauth.weibo")
@Configuration
@Data
public class WeiboOAuthConfig implements Serializable{
    private String appKey;
    private String appSecret;
    private String authSuccessUrl;
    private String authSuccessFail;
    private String authPage;
    private String accessTokenPage;

}
