package com.devlog.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private Integer expirationTime;
    private String tokenPrefix ;
    private String headerString;

    public String getTokenPrefix() {
        return this.tokenPrefix + ' ';
    }
}
