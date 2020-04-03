package star16m.utils.toapp.api.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {
    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:8081", "http://localhost:9803")
                .allowedMethods("*");
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.failOnEmptyBeans(false)
                    .simpleDateFormat(dateTimeFormat)
                    .serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)))
                    .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)))
            ;
        };
    }
}
