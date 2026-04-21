package com.jhost.dev.config;

import com.jhost.dev.bean.ApplicationEnvironment;
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
