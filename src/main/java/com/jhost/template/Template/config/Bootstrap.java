package com.jhost.template.Template.config;

import com.jhost.template.Template.bean.ApplicationEnvironment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Bootstrap implements CommandLineRunner {

    private final ConfigBean configBean;

    public Bootstrap(ConfigBean configBean) {
        this.configBean = configBean;
    }

    @Override
    public void run(String... args) throws Exception {
        logEnv();
    }

    private void logEnv() {
        System.out.println(
                ApplicationEnvironment.isBuilding(configBean.getEnv()) ?
                        "Building project." :
                        "Running environment: " + configBean.getEnv().toString()
        );
    }
}
