package com.itexc.dom.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "jwt")
@Configuration
@Data
public class JwtConfig {

    private String hmacSecret;
    private String issuer;
}
