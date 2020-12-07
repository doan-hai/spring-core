package com.codergeezer.core.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConfig {

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * Class implement for components that are aware of the application's current auditor. This will be some kind of
     * user mostly.
     *
     * @author haidv
     * @version 1.0
     */
    public static class AuditorAwareImpl implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            try {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                return username == null ? Optional.of("anonymous") : Optional.of(username);
            } catch (Exception e) {
                return Optional.of("anonymous");
            }
        }
    }
}
