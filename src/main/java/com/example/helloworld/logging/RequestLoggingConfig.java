package com.example.helloworld.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Structured request logging configuration.
 *
 * <p>YSJP-218: Log each request to /hello using structured logging (no sensitive data).
 * <p>YSJP-214 / HLSA §6: Avoid logging sensitive data; enable structured logging.
 *
 * <p>Logs: method, URI, and client address. Does NOT log request bodies,
 * headers (which may carry auth tokens), or query strings with sensitive data.
 */
@Configuration
public class RequestLoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        // Log client address and URI only – no headers/bodies (avoid sensitive data leakage)
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(false);  // no query params (could contain sensitive data)
        filter.setIncludePayload(false);       // no request body
        filter.setIncludeHeaders(false);       // no headers (could carry tokens)
        filter.setMaxPayloadLength(0);
        return filter;
    }
}
