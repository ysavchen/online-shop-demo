package com.mycompany.online_shop_backend.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter @Setter
@ConfigurationProperties("application.security.token")
public class TokenProperties {

    private String secretKey;
    private long expiration;

}