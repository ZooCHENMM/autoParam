package com.autoparam.config;

import com.autoparam.ApplicationContextProvider;
import com.autoparam.service.*;
import com.clabber.autoparam.*;
import com.clabber.autoparam.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class AutoParamConfig {

    @Bean
    @ConditionalOnMissingBean
    public ResponseParserService responseParserService() {
        return new DefaultResponseParserServiceImpl();
    }

    @Bean
    public ApplicationContextProvider applicationContextProvider() {
        return new ApplicationContextProvider();
    }

    @Bean
    public ServerProperties serverProperties() {
        return new ServerProperties();
    }

    @Bean
    public ResponseFilterService responseFilterService(){
        return new ResponseFilterService();
    }

    @Bean(name = "default")
    public ResponseFilterStrategy responseFilterStrategy(){
        return new DefaultResponseFilterStrategy();
    }

}