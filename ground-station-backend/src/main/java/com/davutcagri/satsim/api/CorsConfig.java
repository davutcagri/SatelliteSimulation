package com.davutcagri.satsim.api;

import com.davutcagri.satsim.config.GroundStationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final GroundStationProperties properties;

    public CorsConfig(GroundStationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(properties.allowedOrigin())
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*");
    }
}
