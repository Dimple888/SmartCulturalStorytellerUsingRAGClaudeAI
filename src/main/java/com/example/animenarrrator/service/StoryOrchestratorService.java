package com.example.animenarrrator.service;

import com.example.animenarrrator.model.Scene;
import com.example.animenarrrator.model.ScenePrompt;
import com.example.animenarrrator.model.StoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Story Orchestrator Service for breaking down narratives into scenes.
 * Orchestrates the transformation of a complete story into scene-by-scene anime prompts.
 * 
 * Features:
 * - Extracts scenes from narrative text
 * - Generates emotional mappings (Rasa)
 * - Creates anime-specific image prompts
 * - Generates audio narration scripts
 */
@Service
public class StoryOrchestratorService {

    private final ClaudeAiService claudeAiService;
    private final PromptTemplateService promptTemplateService;
    private final VectorStoreRagService vectorStoreRagService;
    private final ObjectMapper objectMapper;

    @Autowired
    public StoryOrchestratorService(ClaudeAiService claudeAiService,
                                     PromptTemplateService promptTemplateService,
                                     VectorStoreRagService vectorStoreRagService) {
        this.claudeAiService = claudeAiService;
        this.promptTemplateService = promptTemplateService;
        this.vectorStoreRagService = vectorStoreRagService;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Orchestrate a complete story into scenes with all multimedia elements.
     * 
     * @param storyNarration The complete story text
     * @param storyId Story identifier
     * @param storyTitle Story title
     * @return Complete StoryResponse with all scenes
     */
    public StoryResponse orchestrateStory(String storyNarration, String storyId, String storyTitle) {
        // Extract scenes from the story
        List<Scene> scenes = extractScenes(storyNarration);
        
        // Enhance each scene with anime-specific elements
        scenes = scenes.stream()
            .map(this::enrichSceneWithAnimeElements)
            .collect(Collectors.toList());
        
        // Determine primary emotion from all scenes
        String primaryEmotion = determinePrimaryEmotion(scenes);
        
        // Create and return response
        StoryResponse response = new StoryResponse();
        response.setTitle(storyTitle);
        response.setStoryId(storyId);
        response.setCulturalSource("Sundarakanda / Ramayana");
        response.setEmotion(primaryEmotion);
        response.setScenes(scenes);
        
        return response;
    }

    /**
     * Extract individual scenes from narrative text.
     */
    private List<Scene> extractScenes(String storyNarration) {
        List<Scene> scenes = new ArrayList<>();
        
        // Simple scene splitting logic (can be enhanced with LLM)
        String[] paragraphs = storyNarration.split("\n\n");
        
        int sceneNumber = 1;
        // Character and location lists for simple extraction
        String[] knownCharacters = {"Hanuman", "Sita", "Rama", "Ravana", "Indrajit", "Kumbhakarna"};
        String[] knownLocations = {"Lanka", "forest", "mountain", "ocean", "Ashoka grove", "palace", "battlefield"};

        for (String paragraph : paragraphs) {
            if (paragraph.trim().length() > 100) {  // Skip very short paragraphs
                Scene scene = new Scene();
                scene.setSceneNumber(sceneNumber);
                scene.setNarration(paragraph.trim());
                scene.setEmotion("Veeram");  // Default emotion, will be updated

                // Extract characters present in the scene
                List<String> foundCharacters = new ArrayList<>();
                for (String character : knownCharacters) {
                    if (paragraph.contains(character)) {
                        foundCharacters.add(character);
                    }
                }
                scene.setCharacters(foundCharacters.isEmpty() ? null : String.join(", ", foundCharacters));

                // Extract location present in the scene
                String foundLocation = null;
                for (String location : knownLocations) {
                    if (paragraph.toLowerCase().contains(location.toLowerCase())) {
                        foundLocation = location;
                        break;
                    }
                }
                scene.setLocation(foundLocation);

                scenes.add(scene);
                sceneNumber++;
                if (sceneNumber > 10) break;  // Limit to 10 scenes
            }
        }
        return scenes;
    }

    /**
     * Enrich a scene with anime-specific elements using Claude AI.
     */
    private Scene enrichSceneWithAnimeElements(Scene scene) {
        // Generate image prompt
        String imagePromptTemplate = promptTemplateService.getAnimeImagePromptTemplate(
            scene.getNarration(),
            scene.getEmotion(),
            "Characters in scene",
            "Location"
        );
        String imagePrompt = claudeAiService.generateImagePrompt(imagePromptTemplate);
        scene.setImagePrompt(imagePrompt.trim());
        
        // Generate audio narration
        String audioTemplate = promptTemplateService.getAudioNarrationTemplate(
            scene.getNarration(),
            "Characters",
            "Location"
        );
        String audioText = claudeAiService.generateAudioNarration(audioTemplate);
        scene.setAudioText(audioText.trim());
        
        // Determine emotion from scene content
        scene.setEmotion(determineSceneEmotion(scene.getNarration()));
        
        return scene;
    }

    /**
     * Determine the emotional tone (Rasa) of a scene.
     */
    private String determineSceneEmotion(String sceneText) {
        String lower = sceneText.toLowerCase();
        
        // Simple keyword matching for emotion detection
        if (lower.contains("leap") || lower.contains("courage") || lower.contains("mighty")) {
            return "Veeram";  // Heroism
        } else if (lower.contains("love") || lower.contains("affection") || lower.contains("devotion")) {
            return "Shringar";  // Love
        } else if (lower.contains("sorrow") || lower.contains("sadness") || lower.contains("compassion")) {
            return "Karuna";  // Compassion
        } else if (lower.contains("fear") || lower.contains("terror") || lower.contains("dread")) {
            return "Bhaya";  // Fear
        } else if (lower.contains("joy") || lower.contains("laugh") || lower.contains("delight")) {
            return "Hasya";  // Joy
        } else if (lower.contains("wonder") || lower.contains("awe") || lower.contains("amazed")) {
            return "Adbhuta";  // Wonder
        } else if (lower.contains("anger") || lower.contains("fury") || lower.contains("rage")) {
            return "Raudra";  // Anger
        } else if (lower.contains("disgust") || lower.contains("revulsion")) {
            return "Bibhatsa";  // Disgust
        }
        
        return "Veeram";  // Default to Heroism
    }

    /**
     * Determine primary emotion across all scenes.
     */
    private String determinePrimaryEmotion(List<Scene> scenes) {
        Map<String, Integer> emotionCount = new HashMap<>();
        
        for (Scene scene : scenes) {
            String emotion = scene.getEmotion();
            emotionCount.put(emotion, emotionCount.getOrDefault(emotion, 0) + 1);
        }
        
        return emotionCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Veeram");
    }

    /**
     * Convert scenes to ScenePrompt format for anime generation APIs.
     */
    public List<ScenePrompt> convertScenestoPrompts(List<Scene> scenes) {
        return scenes.stream()
            .map(scene -> {
                ScenePrompt prompt = new ScenePrompt();
                prompt.setSceneNumber(scene.getSceneNumber());
                prompt.setNarration(scene.getNarration());
                prompt.setEmotion(scene.getEmotion());
                prompt.setImagePrompt(scene.getImagePrompt());
                prompt.setAudioText(scene.getAudioText());
                return prompt;
            })
            .collect(Collectors.toList());
    }

    /**
     * Generate a structured scene breakdown with JSON output.
     */
    public String generateSceneBreakdown(String storyNarration) {
        String scenePrompt = promptTemplateService.getSceneExtractionPrompt(storyNarration);
        return claudeAiService.extractScenes(scenePrompt);
    }

    /**
     * Validate story quality and completeness.
     */
    public Map<String, Object> validateStory(StoryResponse story) {
        Map<String, Object> validation = new HashMap<>();
        
        validation.put("hasTitle", story.getTitle() != null && !story.getTitle().isEmpty());
        validation.put("hasScenes", story.getScenes() != null && !story.getScenes().isEmpty());
        validation.put("sceneCount", story.getScenes() != null ? story.getScenes().size() : 0);
        validation.put("emotionsDistributed", validateEmotionDistribution(story.getScenes()));
        validation.put("allScenesComplete", validateSceneCompleteness(story.getScenes()));
        
        return validation;
    }

    /**
     * Check if emotions are well distributed across scenes.
     */
    private boolean validateEmotionDistribution(List<Scene> scenes) {
        if (scenes == null || scenes.isEmpty()) return false;
        
        Set<String> uniqueEmotions = scenes.stream()
            .map(Scene::getEmotion)
            .collect(Collectors.toSet());
        
        return uniqueEmotions.size() >= 2;  // At least 2 different emotions
    }

    /**
     * Check if all scenes have complete information.
     */
    private boolean validateSceneCompleteness(List<Scene> scenes) {
        if (scenes == null || scenes.isEmpty()) return false;
        
        return scenes.stream().allMatch(scene -> 
            scene.getSceneNumber() != null &&
            scene.getNarration() != null && !scene.getNarration().isEmpty() &&
            scene.getEmotion() != null &&
            scene.getImagePrompt() != null && !scene.getImagePrompt().isEmpty() &&
            scene.getAudioText() != null && !scene.getAudioText().isEmpty()
        );
    }
}
