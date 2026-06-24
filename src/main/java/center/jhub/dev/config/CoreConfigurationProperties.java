package center.jhub.dev.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

/**
 * <p>
 *     This is going to be the general Object to access the "core" application properties.
 *     It will have no functionality and shall not be used outside of the package, for that use the wrapper {@link ConfigBean}
 * </p>
 */
@ConfigurationProperties("application.core")
@Getter
class CoreConfigurationProperties {
    private final String env;
    private final String version;

    @ConstructorBinding
    public CoreConfigurationProperties(String env, String version) {
        this.env = env;
        this.version = version;
    }
}
