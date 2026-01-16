package com.example.animenarrrator.service;

import com.example.animenarrrator.model.ScenePrompt;
import com.example.animenarrrator.model.StoryNarration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for generating anime-specific prompts from story narration.
 * Creates detailed prompts suitable for anime generation, image synthesis, and TTS.
 */
@Service
public class AnimePromptService {

    private final StoryOrchestratorService storyOrchestratorService;
    private final ClaudeAiService claudeAiService;
    private final PromptTemplateService promptTemplateService;

    @Autowired
    public AnimePromptService(StoryOrchestratorService storyOrchestratorService,
                             ClaudeAiService claudeAiService,
                             PromptTemplateService promptTemplateService) {
        this.storyOrchestratorService = storyOrchestratorService;
        this.claudeAiService = claudeAiService;
        this.promptTemplateService = promptTemplateService;
    }

    /**
     * Generate scene prompts from a complete story narration.
     */
    public List<ScenePrompt> generateScenePrompts(StoryNarration storyNarration) {
        // Orchestrate the story into scenes
        var storyResponse = storyOrchestratorService.orchestrateStory(
            storyNarration.getContent(),
            "story_001",
            storyNarration.getTitle()
        );
        
        // Convert scenes to prompts
        return storyOrchestratorService.convertScenestoPrompts(storyResponse.getScenes());
    }

    /**
     * Generate a single scene prompt with full details.
     */
    public ScenePrompt generateSingleScenePrompt(String sceneNarration, Integer sceneNumber) {
        ScenePrompt prompt = new ScenePrompt();
        prompt.setSceneNumber(sceneNumber);
        prompt.setNarration(sceneNarration);
        
        // Generate image prompt
        String imagePromptTemplate = promptTemplateService.getAnimeImagePromptTemplate(
            sceneNarration,
            "Veeram",
            "Characters",
            "Setting"
        );
        prompt.setImagePrompt(claudeAiService.generateImagePrompt(imagePromptTemplate));
        
        // Generate audio narration
        String audioTemplate = promptTemplateService.getAudioNarrationTemplate(
            sceneNarration,
            "Characters",
            "Setting"
        );
        prompt.setAudioText(claudeAiService.generateAudioNarration(audioTemplate));
        
        return prompt;
    }

    /**
     * Generate multiple scene prompts for batch processing.
     */
    public List<ScenePrompt> generateBatchScenePrompts(List<String> sceneTexts) {
        List<ScenePrompt> prompts = new ArrayList<>();
        
        for (int i = 0; i < sceneTexts.size(); i++) {
            ScenePrompt prompt = generateSingleScenePrompt(sceneTexts.get(i), i + 1);
            prompts.add(prompt);
        }
        
        return prompts;
    }

    /**
     * Enhance a scene prompt with additional anime styling details.
     */
    public ScenePrompt enhanceScenePromptWithStyle(ScenePrompt prompt, String animeStyle) {
        if (animeStyle != null && !animeStyle.isEmpty()) {
            prompt.setStyle(animeStyle);
            
            // Add style hint to image prompt
            String enhancedImagePrompt = prompt.getImagePrompt() + 
                String.format("\nStyle: %s anime, cinematic quality", animeStyle);
            prompt.setImagePrompt(enhancedImagePrompt);
        }
        
        return prompt;
    }

    /**
     * Generate culturally appropriate character descriptions.
     */
    public String generateCharacterDescription(String sceneContext, String characterName) {
        String prompt = String.format("""
            You are creating character descriptions for anime based on Indian epics.
            
            Scene: %s
            Character: %s
            
            Create a brief character description for anime:
            1. Physical appearance
            2. Cultural attire
            3. Emotional state
            4. Key characteristics
            """, sceneContext, characterName);
        
        return claudeAiService.callLlm(prompt);
    }

    /**
     * Generate location descriptions for anime rendering.
     */
    public String generateLocationDescription(String sceneContext, String location) {
        String prompt = String.format("""
            You are creating environment descriptions for anime rendering.
            
            Scene: %s
            Location: %s
            
            Create a detailed location description for 3D anime environments:
            """, sceneContext, location);
        
        return claudeAiService.callLlm(prompt);
    }

    /**
     * Generate color palette recommendations.
     */
    public String generateColorPalette(String emotion, String location) {
        String prompt = String.format("""
            You are a color expert for anime production.
            
            Emotion: %s
            Location: %s
            
            Suggest a color palette (5-7 colors with hex codes).
            """, emotion, location);
        
        return claudeAiService.callLlm(prompt);
    }
}
