package com.example.animenarrrator.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Spring AI Configuration for Claude Haiku integration.
 * Configures ChatModel and other AI-related beans.
 */
@Configuration
public class SpringAiConfig {

    /**
     * RestTemplate bean for HTTP communication.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * ChatModel is auto-configured by Spring AI for Anthropic Claude.
     * Ensure ANTHROPIC_API_KEY is set in environment variables.
     */
}
