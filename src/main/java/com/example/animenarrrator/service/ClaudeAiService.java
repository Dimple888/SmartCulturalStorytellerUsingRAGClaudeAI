package com.example.animenarrrator.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for interacting with Claude Haiku via Spring AI.
 * Handles all LLM calls for story generation and scene orchestration.
 * Includes fallback responses if API authentication fails or demo mode is enabled.
 */
@Service
public class ClaudeAiService {

    private static final Logger logger = LoggerFactory.getLogger(ClaudeAiService.class);
    private final ChatClient chatClient;
    
    @Value("${app.demo-mode:false}")
    private boolean demoMode;

    @Autowired
    public ClaudeAiService(ChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    /**
     * Generate story content from cultural context and user query.
     * Includes fallback if Claude API fails due to auth issues or demo mode.
     */
    public String generateStory(String prompt) {
        if (demoMode) {
            logger.info("Demo mode enabled - using fallback story response");
            return generateFallbackStory();
        }
        
        try {
            return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
        } catch (Exception e) {
            logger.error("Claude API call failed. Ensure ANTHROPIC_API_KEY is set. Error: {}", e.getMessage());
            return generateFallbackStory();
        }
    }

    /**
     * Extract scenes from a story narrative.
     */
    public String extractScenes(String prompt) {
        if (demoMode) {
            return "[Scene 1: Opening]\n[Scene 2: Development]\n[Scene 3: Climax]\n[Scene 4: Resolution]";
        }
        
        try {
            return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
        } catch (Exception e) {
            logger.error("Failed to extract scenes: {}", e.getMessage());
            return "[Scene 1: Opening]\n[Scene 2: Development]\n[Scene 3: Climax]\n[Scene 4: Resolution]";
        }
    }

    /**
     * Generate anime image prompt for a scene.
     */
    public String generateImagePrompt(String prompt) {
        if (demoMode) {
            return "Anime scene with vibrant colors, cinematic lighting, detailed character expressions, dynamic composition, dramatic shadows";
        }
        
        try {
            return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
        } catch (Exception e) {
            logger.error("Failed to generate image prompt: {}", e.getMessage());
            return "Anime scene with vibrant colors, cinematic lighting, detailed character expressions, dynamic composition, dramatic shadows";
        }
    }

    /**
     * Generate audio narration text for a scene.
     */
    public String generateAudioNarration(String prompt) {
        if (demoMode) {
            return "In this moment, we witness a tale of courage and devotion unfold...";
        }
        
        try {
            return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
        } catch (Exception e) {
            logger.error("Failed to generate audio narration: {}", e.getMessage());
            return "In this moment, we witness a tale of courage and devotion unfold...";
        }
    }

    /**
     * Generic method for any custom prompt.
     */
    public String callLlm(String prompt) {
        if (demoMode) {
            return "Response unavailable - demo mode enabled. Set app.demo-mode=false and provide ANTHROPIC_API_KEY to use Claude AI.";
        }
        
        try {
            return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
        } catch (Exception e) {
            logger.error("LLM call failed: {}. Check ANTHROPIC_API_KEY env variable.", e.getMessage());
            return "Response unavailable - please verify ANTHROPIC_API_KEY is set correctly.";
        }
    }

    /**
     * Fallback story when Claude API is unavailable or demo mode is enabled.
     */
    private String generateFallbackStory() {
        return "Hanuman stood upon Mount Mahendra, his eyes fixed upon the vast ocean before him. " +
               "With divine determination, he expanded his form and leaped skyward across the endless waters. " +
               "Though demons challenged his path, his unwavering devotion to Rama kept him focused on his sacred mission. " +
               "At last, the golden city of Lanka appeared before him, a realm of wonder and mystery. " +
               "In the Ashoka garden, he discovered Sita, her spirit unbroken despite her captivity. " +
               "By offering Rama's signet ring, he rekindled hope in her heart. " +
               "With righteous fury, Hanuman demonstrated Rama's power throughout Lanka's halls. " +
               "Victorious, he returned with news of Sita's location, his mission complete and his devotion rewarded.";
    }
}
