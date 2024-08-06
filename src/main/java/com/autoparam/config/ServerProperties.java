package com.autoparam.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "autoparam", ignoreInvalidFields = true)
public class ServerProperties {

    @Value("${filter.strategy:default}")
    private String filterStrategy;
}
