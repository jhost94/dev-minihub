package com.jhost.template.Template.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * <p>
 *     This is going to be the general Object to access the "core" application properties.
 *     It will have no functionality and shall not be used outside of the package, for that use the wrapper {@link ConfigBean}
 * </p>
 */
@ConstructorBinding
@ConfigurationProperties("application.core")
@Getter
@AllArgsConstructor
class CoreConfigurationProperties {
    private final String env;
}
