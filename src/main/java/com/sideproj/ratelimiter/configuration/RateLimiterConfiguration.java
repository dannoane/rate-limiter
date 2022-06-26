package com.sideproj.ratelimiter.configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "configuration")
@PropertySource(value = "classpath:configuration.yml", factory = YamlPropertySourceFactory.class)
public class RateLimiterConfiguration {
    @Getter @Setter
    private String targetProtocol;
    @Getter @Setter @NonNull
    private String targetHost;
    @Getter @Setter
    private String targetPort;

    public String getTargetUrl() {
        StringBuilder url = new StringBuilder();

        if (targetProtocol != null) {
            url.append(targetProtocol);
            url.append("://");
        }

        url.append(targetHost);

        if (targetPort != null) {
            url.append(":");
            url.append(targetPort);
        }

        return url.toString();
    }
}
