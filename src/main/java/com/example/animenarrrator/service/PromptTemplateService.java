package com.example.animenarrrator.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for managing prompt templates for story generation and scene orchestration.
 * Contains templates for LLM prompts used with Claude Haiku via Spring AI.
 */
@Service
public class PromptTemplateService {

    /**
     * Template for generating a complete story from cultural text chunks.
     * Maintains cultural authenticity and emotional depth.
     */
    public String getStoryGenerationPrompt(String culturalContext, String userQuery) {
        return String.format("""
            You are an expert storyteller specializing in cultural narratives and ancient epics.
            
            CULTURAL CONTEXT:
            %s
            
            USER REQUEST:
            %s
            
            INSTRUCTIONS:
            1. Generate a coherent, engaging story based on the cultural context
            2. Maintain cultural authenticity and preserve traditional elements
            3. Use simple, engaging language suitable for anime narration
            4. Preserve key emotions: devotion, heroism, compassion, wisdom
            5. Keep the story structured and scene-ready
            6. Focus on emotional depth and character development
            
            STORY:
            """, culturalContext, userQuery);
    }

    /**
     * Template for breaking down a story into individual scenes.
     * Extracts emotional tone and visual elements for anime generation.
     */
    public String getSceneExtractionPrompt(String storyNarration) {
        return String.format("""
            You are an expert at breaking down narratives into visual scenes for anime production.
            
            STORY:
            %s
            
            TASK:
            Break this story into 5-8 distinct scenes. For each scene, provide:
            1. Scene number
            2. Narration text (2-3 sentences)
            3. Emotion/Rasa (Veeram/Heroism, Shringar/Love, Karuna/Compassion, Bhaya/Fear, Hasya/Joy, Adbhuta/Wonder, Raudra/Anger, Bibhatsa/Disgust)
            4. Anime image prompt (descriptive, cinematic style)
            5. Audio narration text
            6. Key characters
            7. Location/Setting
            
            Format as JSON array of scenes.
            """, storyNarration);
    }

    /**
     * Template for generating anime-specific image prompts.
     * Creates detailed visual descriptions suitable for image generation models.
     */
    public String getAnimeImagePromptTemplate(String scene, String emotion, String characters, String location) {
        return String.format("""
            You are an expert in creating anime visual prompts.
            
            Scene: %s
            Emotion/Mood: %s
            Characters: %s
            Location: %s
            
            Create a detailed anime-style image prompt that:
            1. Is cinematic and visually compelling
            2. Captures the emotional tone
            3. Includes visual style hints (anime, cinematic, ethereal, dynamic)
            4. References cultural elements appropriately
            5. Is under 150 words
            
            IMAGE PROMPT:
            """, scene, emotion, characters, location);
    }

    /**
     * Template for generating audio narration text.
     * Ensures text is clear and suitable for text-to-speech conversion.
     */
    public String getAudioNarrationTemplate(String scene, String characters, String location) {
        return String.format("""
            You are creating narration for a devotional anime.
            
            Scene Context:
            %s
            
            Characters: %s
            Location: %s
            
            Create a clear, engaging narration script that:
            1. Is suitable for voice acting
            2. Captures emotional nuance
            3. Is 30-60 seconds when read aloud
            4. Includes cultural references appropriately
            5. Is easy to pronounce
            
            NARRATION:
            """, scene, characters, location);
    }

    /**
     * Gets all available emotion/Rasa types in Indian classical arts.
     */
    public Map<String, String> getEmotionMap() {
        Map<String, String> emotions = new HashMap<>();
        emotions.put("Veeram", "Heroism - Courage, valor, and strength");
        emotions.put("Shringar", "Love - Romance, affection, and attraction");
        emotions.put("Karuna", "Compassion - Sadness, empathy, and sorrow");
        emotions.put("Bhaya", "Fear - Dread, apprehension, and terror");
        emotions.put("Hasya", "Joy - Laughter, humor, and delight");
        emotions.put("Adbhuta", "Wonder - Amazement, fascination, and awe");
        emotions.put("Raudra", "Anger - Rage, fury, and indignation");
        emotions.put("Bibhatsa", "Disgust - Revulsion and contempt");
        return emotions;
    }

    /**
     * Gets prompt for initial cultural data loading.
     */
    public String getCulturalDataLoadingPrompt() {
        return """
            You are processing cultural epic data for storytelling.
            
            For each story chunk, extract and structure:
            1. Verse/Key passage
            2. Meaning/Interpretation
            3. Emotional tone (Rasa)
            4. Main characters
            5. Scene intent (action, dialogue, contemplation, etc.)
            6. Location/Setting
            
            Ensure all data preserves cultural authenticity and emotional depth.
            """;
    }
}
