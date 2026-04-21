package com.jhost.template.Template.config;

import com.jhost.template.Template.bean.ApplicationEnvironment;
import lombok.Getter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@EnableConfigurationProperties(CoreConfigurationProperties.class)
@Configuration
public class ConfigBean {

    private final ApplicationEnvironment env;

    public ConfigBean(CoreConfigurationProperties coreConfigurationProperties){
        this.env = ApplicationEnvironment.fromString(coreConfigurationProperties.getEnv());
    }
}
