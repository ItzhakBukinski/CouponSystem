package com.jb.couponSystem.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

/**
 * A configuration class for producing the {@link HashMap} where all the {@link ClientSession}s will be stored.
 */
@EnableScheduling
@Configuration
public class RestConfiguration {

    /**
     * Creating the {@link HashMap} where all the {@link ClientSession}s will be stored
     * with a token for a limited time session.
     *
     * @return the {@link HashMap}.
     */
    @Bean
    public Map<String, ClientSession> tokensMap() {
        return new HashMap<>();
    }
}