package com.autoparam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "autoparam", ignoreInvalidFields = true)
public class ServerProperties {

    @Value("${filter.strategy:default}")
    private String filterStrategy;

    public String getFilterStrategy() {
        return filterStrategy;
    }

    public void setFilterStrategy(String filterStrategy) {
        this.filterStrategy = filterStrategy;
    }
}
