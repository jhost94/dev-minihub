package center.jhub.dev.bean;

import center.jhub.utils.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMappers.getInstance();
    }
}
