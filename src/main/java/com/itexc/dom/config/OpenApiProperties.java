package com.itexc.dom.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.*;

@ConfigurationProperties(prefix = "openapi")
@AllArgsConstructor
@Getter
public class OpenApiProperties {

    private final String projectTitle;
    private final String projectDescription;
    private final String projectVersion;
}
